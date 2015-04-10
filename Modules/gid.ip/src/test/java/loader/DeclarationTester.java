package loader;

import de.akquinet.jbosscc.needle.annotation.InjectIntoMany;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import loader.base.CoreIpModelTester;
import loader.entity.EasyDeclaration;
import org.junit.Test;
import softclub.model.DeclarationDao;
import softclub.model.InputPaymentDao;
import softclub.model.OutputPaymentDao;
import softclub.model.entities.Declaration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by gid_000 on 23.01.14.
 */
public class DeclarationTester extends CoreIpModelTester {

    @InjectIntoMany
    @ObjectUnderTest
    InputPaymentDao inputPaymentDao;

    @InjectIntoMany
    @ObjectUnderTest
    OutputPaymentDao outputPaymentDao;

    @InjectIntoMany
    @ObjectUnderTest
    protected DeclarationDao declarationDao;

    public static final String DECLARATION_SQL =
            "select IMONTH,IYEAR,S1,S2,S2_1,S3,S3_1,S4,S5 " +
            "from T_EASY_DECL_NEW " +
            "where IYEAR is not null and IMONTH is not null " +
            "order by IYEAR, IMONTH ";

    @Test
    public void declarationOneTest(){
        connectTest();
        Declaration old = declarationDao.findDeclaration(declarationDao.getDate4Params(0, 2014));
        if(old==null){
            old = new Declaration();
        }
        newEm.getTransaction().begin();
        Declaration newDecl = declarationDao.calcDeclaration(2014, 4, new Declaration());
        assertEquals(2960000, newDecl.getTotalInputYear(), 1);
        newEm.getTransaction().commit();
    }

    /**предварительно выполнить загрузку данных  IpModelTester.reloadData()
     * и отключить пересоздание таблиц
     * TODO: Тест сверки сумм по декларациям старой и новой моделей
     */
    @Test
    public void declarationTest(){
        connectTest();
        assertNotNull(declarationDao);
        EntityManagerFactory oldFactory = Persistence.createEntityManagerFactory("OldModelIP");
        EntityManager oldEm = oldFactory.createEntityManager();
        assertNotNull(oldEm);

        List<EasyDeclaration> oldList = oldEm.createNativeQuery(DECLARATION_SQL, EasyDeclaration.class).getResultList();
        assertNotNull(oldList);
        Declaration oldDecl = new Declaration();
        for (EasyDeclaration old: oldList) {
            Declaration newDecl = declarationDao.calcDeclaration(old.getId().getYear(), old.getId().getMonth(), oldDecl);
            newEm.getTransaction().begin();
            newEm.merge(newDecl);
            assertEquals(old.getS5(), newDecl.getNalog(), 0.01);
            LOGGER.info(old.getId().getMonth() + "-" + old.getId().getYear());
            newEm.getTransaction().commit();
            oldDecl = old.getId().getMonth()==12? new Declaration(): newDecl;
        }
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
