import org.apache.log4j.Logger;
import softclub.model.entities.Document;
import softclub.model.entities.pk.DocumentId;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: all
 * Date: 02.02.13
 * Time: 18:17
 * To change this template use File | Settings | File Templates.
 */
public class TestModelNew {
    private static final Logger LOGGER = Logger.getLogger(TestModelNew.class);
    private static final String PERSISTENCE_UNIT_NAME = "NewModelIP";
    private static EntityManagerFactory factory, oldFactory;

    //@Test
    public void t1(){

        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        assertNotNull(em);
        oldFactory = Persistence.createEntityManagerFactory("OldModelIP");
        EntityManager oldEm = oldFactory.createEntityManager();
        assertNotNull(oldEm);
        // Read the existing entries and write to console
        addTestDocument(oldEm, em);

        List<Document> list = queryListObjects(em, Document.class);
        for (Document payment : list){
            LOGGER.info("p=" + payment.toString());
        }

        em.close();
    }

    private void addTestDocument(EntityManager oldEm, EntityManager em) {
        Query oldDocumentQuey = oldEm.createNativeQuery("select * from In_pp");
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        for(Object o :oldDocumentQuey.getResultList()) {
            Object[] attr = (Object[]) o;
            Document document = new Document();

            document.setId(
                    new DocumentId(
                            new Date(((java.sql.Timestamp) attr[1]) .getTime()),
                            (String) attr[0]
                    )
            );
//            document.ge

            em.persist(document);
        }

//        Document document = new Document();
//        document.setDocNumber("1");
        em.flush();
        transaction.commit();
    }


    private <E> List<E> queryListObjects (EntityManager em, Class<E> tClass){
        CriteriaBuilder qb = em.getCriteriaBuilder();
        CriteriaQuery<E> query = qb.createQuery(tClass);
        Root<E> employee = query.from(tClass);
        //query.where(qb.equal(employee.get("firstName"), "Bob"));
        final List<E> result = em.createQuery(query).getResultList();
        return result;
    }

    private <T> List<T> getList(EntityManager em, Class<T> tClass) {
        CriteriaBuilder cr = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cr.createQuery(tClass);
        Root<T> root = q.from(tClass);
        return em.createQuery(q).getResultList();
    }

}
