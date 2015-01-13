package loader.base;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;
import de.akquinet.jbosscc.needle.junit.NeedleRule;
import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import softclub.model.SessionEJBBean;
import softclub.model.entities.Document;
import softclub.model.entities.pk.DocumentId;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.sql.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: gid
 * Date: 22.09.13
 * Time: 13:33
 * To change this template use File | Settings | File Templates.
 */
public class CoreIpModelTester {
    @Rule
    public DatabaseRule newDatabaseRule = new DatabaseRule("NewModelIP");
    @Rule
    public NeedleRule needleRule = new NeedleRule(newDatabaseRule);
    @Rule
    public DatabaseRule oldDatabaseRule = new DatabaseRule("OldModelIP");
    @Rule
    public NeedleRule oldNeedleRule = new NeedleRule(oldDatabaseRule);
    @ObjectUnderTest(implementation = SessionEJBBean.class)
    protected SessionEJBBean newModel;
    @Inject
    protected javax.persistence.EntityManager newEm;
    //    Logger LOGGER = Logger.getRootLogger();
    protected Logger LOGGER = Logger.getLogger("loader.IpModelTester");


    @Test
    public void connectTest(){
        EntityManager oldEm = oldDatabaseRule.getEntityManager();
        //EntityManager newEm = newDatabaseRule.getEntityManager();

        assertNotNull(oldEm);
        assertNotNull(newModel);
        assertNotNull(newEm);

        Query oldQ = oldEm.createNativeQuery("select USER from dual");
        String user = (String) oldQ.getSingleResult();
        assertEquals("GID", user);

        String driver = (String) newEm.getEntityManagerFactory().getProperties().get("javax.persistence.jdbc.driver");
        if("oracle.jdbc.driver.OracleDriver".equals(driver)) {
            Query newQ = newEm.createNativeQuery("select USER from dual");
            user = (String) newQ.getSingleResult();
            assertEquals("GIDPOST", user);
        }
    }



    protected  <T extends Document> T findDoc(String docNumber, Date docDate, T newDoc) {
        T doc = null;
        if(docNumber!=null || docDate!=null){
            doc = (T) newEm.find(Document.class, new DocumentId(docDate, docNumber));
            doc = (doc==null? newDoc: doc);

            DocumentId docId = new DocumentId(docDate, docNumber);
            doc.setId(docId);
        }
        return doc;
    }

}
