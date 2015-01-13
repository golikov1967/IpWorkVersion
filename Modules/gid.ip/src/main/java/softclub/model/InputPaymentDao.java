package softclub.model;

import by.softclub.fos.model.dao.base.AbstractDao;
import softclub.model.entities.Act;
import softclub.model.entities.Contract;
import softclub.model.entities.Document;
import softclub.model.entities.InputPayment;
import softclub.model.entities.InputPayment_;
import softclub.model.entities.Payment_;
import softclub.model.entities.pk.DocumentId;
import softclub.model.entities.pk.DocumentId_;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.logging.Logger;

@Stateless
public class InputPaymentDao extends AbstractDao<InputPayment, DocumentId> {

    private static final String NOT_PARSED = "Не разобрано:";

    private static final Logger LOGGER = Logger.getAnonymousLogger();


    public InputPaymentDao() {
        super(InputPayment.class);
    }

    public BigDecimal getTestSum4Date(int currYear, int begMonth, int endMonth) {
        return getInputSum4Date(currYear, begMonth, endMonth);
    }


    /**
     * @param endMonth
     * @param currYear
     * @return select NVL(sum(t.doc_sum), 0)
     * into qStr1
     * from in_pp t
     * where t.akt_num is not null
     * and to_char(t.doc_date, 'yyyy') = currYear
     * and to_char(nvl(t.oper_date, t.doc_date), 'mm') <= cMonth;
     */
    public BigDecimal getInputSum4Date(int currYear, int begMonth, int endMonth) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> criteriaQuery = cb.createQuery(BigDecimal.class);
        Root<InputPayment> root = criteriaQuery.from(type);


        criteriaQuery.where(
                cb.and(
                        cb.isNotNull(root.get(InputPayment_.act)),
                        cb.equal(
                                cb.function(
                                        "to_char",
                                        String.class,
                                        root.get(InputPayment_.id).get(DocumentId_.docDate), cb.literal("yyyy")),
                                Integer.toString(currYear)
                        ),
                        cb.greaterThan(
                                cb.function(
                                        "to_number",
                                        Integer.class,
                                        cb.function(
                                                "to_char",
                                                String.class,
                                                cb.function(
                                                        "nvl",
                                                        Date.class,
                                                        root.get(Payment_.applyDate), root.get(Payment_.id).get(DocumentId_.docDate)),
                                                cb.literal("mm"))
                                ),
                                begMonth
                        ),
                        cb.lessThanOrEqualTo(
                                cb.function(
                                        "to_number",
                                        Integer.class,
                                        cb.function(
                                                "to_char",
                                                String.class,
                                                cb.function(
                                                        "nvl",
                                                        Date.class,
                                                        root.get(Payment_.applyDate), root.get(Payment_.id).get(DocumentId_.docDate)),
                                                cb.literal("mm"))
                                ),
                                endMonth
                        )
                )
        );

        criteriaQuery.select(cb.sum(root.get(Payment_.paySum)));
        TypedQuery<BigDecimal> query = em.createQuery(criteriaQuery);
        BigDecimal result = query.getSingleResult();

        return result==null? BigDecimal.ZERO : result;
    }

    public void fromSoftclub(String nDoc, Date payDate, double paySumm, String contractNumber, Date contractDate, String actNumber, Date actDate) throws ParseException {
        InputPayment inputPayment = new InputPayment();
        inputPayment.setId(new DocumentId(payDate, nDoc));
        inputPayment.setPaySum(BigDecimal.valueOf(paySumm));
        Act act = findDoc(actNumber, actDate, new Act());
        Contract contract = findDoc(contractNumber, contractDate, new Contract());
        act.setContract(contract);
        inputPayment.setAct(act);
        inputPayment.setPayNote(MessageFormat.format("ОПЛАТА СОГЛАСНО ДОГ. {0} ОТ {1} ЗА ТЕСТИРОВАНИЕ ПО СОГЛАСНО АКТУ {2} ОТ {3}Г. ЦЕНЫ СОГЛАСНО П.3.3 ДОГОВОРА. БЕЗ НДС.",
                contractNumber, dateToString(contractDate), actNumber, dateToString(actDate)));
        em.merge(inputPayment);

    }


    protected  <T extends Document> T findDoc(String docNumber, Date docDate, T newDoc) {
        T doc = null;
        if(docNumber!=null || docDate!=null){
            doc = (T) em.find(newDoc.getClass(), new DocumentId(docDate, docNumber));
            doc = (doc==null? newDoc: doc);

            DocumentId docId = new DocumentId(docDate, docNumber);
            doc.setId(docId);
        }
        return doc;
    }

    private String dateToString(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
}
