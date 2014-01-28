package softclub.model;

import by.softclub.fos.model.dao.base.AbstractDao;
import softclub.model.entities.Payment;
import softclub.model.entities.pk.DocumentId;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.logging.Logger;

@Stateless
public class PaymentDao extends AbstractDao<Payment, DocumentId> {

    @EJB
    PaymentDao payDao;

    private static final String NOT_PARSED = "Не разобрано:";

    private static final Logger LOGGER = Logger.getAnonymousLogger();


    public PaymentDao() {
        super(Payment.class);
    }

    public double getInputSum4Date(int iMonth, int currYear) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Double> criteriaQuery = cb.createQuery(Double.class);
        Root<Payment> root = criteriaQuery.from(type);

        //Payment_ e;
        //CriteriaQuery<Double> select = criteriaQuery.select(root.get(Payment_.));
        TypedQuery<Double> query = em.createQuery(criteriaQuery);
        Double result = query.getSingleResult();

        return 0;
    }
}
