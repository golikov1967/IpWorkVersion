package loader;

import de.akquinet.jbosscc.needle.annotation.InjectIntoMany;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import loader.base.CoreIpModelTester;
import org.junit.Test;
import softclub.model.InputPaymentDao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by gid_000 on 23.01.14.
 */
public class InputDocTester extends CoreIpModelTester {

    @InjectIntoMany
    @ObjectUnderTest
    InputPaymentDao inputPaymentDao;


    public static final String DECLARATION_SQL =
            "select IMONTH,IYEAR,S1,S2,S2_1,S3,S3_1,S4,S5 " +
            "from T_EASY_DECL_NEW " +
            "where IYEAR is not null and IMONTH is not null " +
            "order by IYEAR, IMONTH ";

    @Test
    public void fromSoftclub(){
        connectTest();
        assertEquals(1, 1);
    }

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

        Query newQ = newEm.createNativeQuery("select USER from dual");
        user = (String) newQ.getSingleResult();
        assertEquals("GIDPOST", user);
    }

}
