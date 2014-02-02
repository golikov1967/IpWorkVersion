package softclub.model;

import by.softclub.fos.model.dao.base.AbstractDao;
import softclub.model.entities.InputPayment;
import softclub.model.entities.InputPayment_;
import softclub.model.entities.Payment;
import softclub.model.entities.Payment_;
import softclub.model.entities.pk.DocumentId;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.logging.Logger;

@Stateless
public class InputPaymentDao extends AbstractDao<InputPayment, DocumentId> {

    private static final String NOT_PARSED = "Не разобрано:";

    private static final Logger LOGGER = Logger.getAnonymousLogger();


    public InputPaymentDao() {
        super(InputPayment.class);
    }

    public Double getTestSum4Date(int currYear, int begMonth, int endMonth) {
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
    public Double getInputSum4Date(int currYear, int begMonth, int endMonth) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Double> criteriaQuery = cb.createQuery(Double.class);
        Root<InputPayment> root = criteriaQuery.from(type);


        criteriaQuery.where(
                cb.and(
                        cb.isNotNull(root.get(InputPayment_.act)),
                        cb.equal(
                                cb.function(
                                        "to_char",
                                        String.class,
                                        root.get(InputPayment_.docDate), cb.literal("yyyy")),
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
                                                        root.get(Payment_.applyDate), root.get(Payment_.docDate)),
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
                                                        root.get(Payment_.applyDate), root.get(Payment_.docDate)),
                                                cb.literal("mm"))
                                ),
                                endMonth
                        )
                )
        );

        criteriaQuery.select(cb.sum(root.get(Payment_.paySum)));
        TypedQuery<Double> query = em.createQuery(criteriaQuery);
        Double result = query.getSingleResult();

        return result==null? 0d: result;
    }
}
