package softclub.model;

import by.softclub.fos.model.dao.base.AbstractDao;
import softclub.model.entities.Payment;
import softclub.model.entities.Payment_;
import softclub.model.entities.pk.DocumentId;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.logging.Logger;

@Stateless
public class PaymentDao extends AbstractDao<Payment, DocumentId> {

    private static final String NOT_PARSED = "Не разобрано:";

    private static final Logger LOGGER = Logger.getAnonymousLogger();


    public PaymentDao() {
        super(Payment.class);
    }

    /**
     *
     * @param iMonth
     * @param currYear
     * @return
            select NVL(sum(t.doc_sum), 0)
            into qStr1
            from in_pp t
            where t.akt_num is not null
            and to_char(t.doc_date, 'yyyy') = currYear
            and to_char(nvl(t.oper_date, t.doc_date), 'mm') <= cMonth;
     */
    public Double getInputSum4Date(int iMonth, int currYear) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Double> criteriaQuery = cb.createQuery(Double.class);
        Root<Payment> root = criteriaQuery.from(type);


        criteriaQuery.where(
                //cb.and(
                        cb.equal(
                            cb.function(
                                    "to_char",
                                    String.class,
                                    root.get(Payment_.docDate), cb.literal("yyyy")),
                            Integer.toString(currYear)
                        )
        );


        //Payment_ e;
        criteriaQuery.select(cb.sum(root.get(Payment_.paySum)));
        TypedQuery<Double> query = em.createQuery(criteriaQuery);
        Double result = query.getSingleResult();

        return result;
    }
}
