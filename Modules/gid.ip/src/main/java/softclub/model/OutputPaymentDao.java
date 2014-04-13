package softclub.model;

import by.softclub.fos.model.dao.base.AbstractDao;
import softclub.model.entities.OutputPayment;
import softclub.model.entities.OutputPayment_;
import softclub.model.entities.PayType;
import softclub.model.entities.pk.DocumentId;
import softclub.model.entities.pk.DocumentId_;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;

@Stateless
public class OutputPaymentDao extends AbstractDao<OutputPayment, DocumentId> {

    private static final String NOT_PARSED = "Не разобрано:";

    private static final Logger LOGGER = Logger.getAnonymousLogger();


    public OutputPaymentDao() {
        super(OutputPayment.class);
    }

    /**
     * @param iMonth
     * @param currYear
     * @return select qStr1 - NVL(sum(t.doc_sum), 0)
     * into qStr1
     * from out_pp t
     * where t.pay_type_id =
     * (select pt.pay_type_id
     * from pay_types pt
     * where pt.type_code = 'ERROR_PAY')
     * and to_char(nvl(t.oper_date, t.doc_date), 'yyyy') = currYear
     * and to_char(nvl(t.oper_date, t.doc_date), 'mm') <= cMonth; -- сумма возвратов начала года
     */
    public BigDecimal getMinusSum4Date(int iMonth, int currYear) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> criteriaQuery = cb.createQuery(BigDecimal.class);
        Root<OutputPayment> root = criteriaQuery.from(type);

        PayType err = em.find(PayType.class,"ERROR_PAY");

        criteriaQuery.where(
                cb.and(
                        cb.isNotNull(root.get(OutputPayment_.paySum)),
                        cb.equal(root.get(OutputPayment_.payType), err)
                        ,
                        cb.equal(
                                cb.function(
                                        "to_char",
                                        String.class,
                                        root.get(OutputPayment_.id).get(DocumentId_.docDate), cb.literal("yyyy")),
                                Integer.toString(currYear)
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
                                                        root.get(OutputPayment_.applyDate), root.get(OutputPayment_.id).get(DocumentId_.docDate)),
                                                cb.literal("mm"))
                                ),
                                iMonth
                        )
                )
        );

        criteriaQuery.select(cb.sum(root.get(OutputPayment_.paySum)));
        TypedQuery<BigDecimal> query = em.createQuery(criteriaQuery);
        BigDecimal result = query.getSingleResult();

        return result == null? BigDecimal.ZERO: result;
    }
}
