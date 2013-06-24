package loader;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;
import de.akquinet.jbosscc.needle.junit.NeedleRule;
import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import softclub.model.SessionEJB;
import softclub.model.SessionEJBBean;
import softclub.model.entities.Document;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


/**
 * Created with IntelliJ IDEA.
 * User: gid
 * Date: 21.06.13
 * Time: 17:34
 */
public class Loader {
    Logger LOGGER = Logger.getRootLogger();

    @Rule
    public DatabaseRule databaseRule = new DatabaseRule("NewModelIP");

    @Rule
    public NeedleRule needleRule = new NeedleRule(databaseRule);

    @ObjectUnderTest(implementation = SessionEJBBean.class)
    private SessionEJB newModel;

    @Inject
    private javax.persistence.EntityManager newEm;

    @Rule
    public DatabaseRule oldDatabaseRule = new DatabaseRule("OldModelIP");


    @Rule
    public NeedleRule oldNeedleRule = new NeedleRule(oldDatabaseRule);

    @Test
    public void reloadData(){
        LOGGER.error("test");

        EntityManager oldEm = oldDatabaseRule.getEntityManager();
        //EntityManager newEm = databaseRule.getEntityManager();

        assertNotNull(oldEm);
        assertNotNull(newModel);
        assertNotNull(newEm);

        Query oldQ = oldEm.createNativeQuery("select USER from dual");
        String user = (String) oldQ.getSingleResult();
        LOGGER.error("проверка прошла успешно user=" + user);

        Query newQ = newEm.createNativeQuery("select USER from dual");
        user = (String) newQ.getSingleResult();
        LOGGER.error("проверка прошла успешно user=" + user);

        addTestDocument(oldEm, newEm);
    }


    private void addTestDocument(EntityManager oldEm, EntityManager em) {
        Query oldDocumentQuey = oldEm.createNativeQuery("select * from In_pp");
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        for(Object o :oldDocumentQuey.getResultList()) {
            Object[] attr = (Object[]) o;
            Document document = new Document();
            document.setDocNumber((String) attr[0]);
            document.setDocDate(new Date(((java.sql.Timestamp) attr[1]) .getTime()));
//            document.ge

            em.persist(document);
        }

//        Document document = new Document();
//        document.setDocNumber("1");
        em.flush();
        transaction.commit();
        LOGGER.error("загрузка прошла успешно, загружено " + oldDocumentQuey.getResultList().size() + " записей");
    }
}
