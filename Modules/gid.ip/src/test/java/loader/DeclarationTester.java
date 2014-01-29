package loader;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;
import de.akquinet.jbosscc.needle.junit.NeedleRule;
import loader.base.CoreIpModelTester;
import loader.entity.EasyDeclaration;
import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import softclub.model.*;
import softclub.model.entities.Declaration;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by gid_000 on 23.01.14.
 */
public class DeclarationTester{
    @Rule
    public DatabaseRule newDatabaseRule = new DatabaseRule("NewModelIP");
    @Rule
    public NeedleRule needleRule = new NeedleRule(newDatabaseRule);
    @Rule
    public DatabaseRule oldDatabaseRule = new DatabaseRule("OldModelIP");
    @Rule
    public NeedleRule oldNeedleRule = new NeedleRule(oldDatabaseRule);
    @Inject
    protected javax.persistence.EntityManager newEm;
    //    Logger LOGGER = Logger.getRootLogger();
    protected Logger LOGGER = Logger.getLogger("loader.IpModelTester");


    @ObjectUnderTest(implementation = DeclarationDao.class)
    protected DeclarationDao declDao;

    @ObjectUnderTest(implementation = InputPaymentDao.class)
    InputPaymentDao inputPaymentDao;

    @ObjectUnderTest(implementation = OutputPaymentDao.class)
    OutputPaymentDao outputPaymentDao;

    public static final String DECLARATION_SQL =
            "select IMONTH,IYEAR,S1,S2,S2_1,S3,S3_1,S4,S5 " +
            "from T_EASY_DECL_NEW " +
            "where IYEAR is not null and IMONTH is not null " +
            "order by IYEAR, IMONTH ";

    @Test
    public void declarationOneTest(){
        declDao.inputPaymentDao = inputPaymentDao;
        declDao.outputPaymentDao = outputPaymentDao;
        newEm.getTransaction().begin();
        Declaration newDecl = declDao.calcDeclaration(2008, 1);
        assertEquals(2960000, newDecl.getTotalInputFromBeginYear(), 1);
        newEm.getTransaction().commit();
    }

    //TODO Реализовать тест сверки сумм по декларациям старой и новой моделей
    @Test
    public void declarationTest(){
        assertNotNull(declDao);
        declDao.inputPaymentDao = inputPaymentDao;
        declDao.outputPaymentDao = outputPaymentDao;
        EntityManagerFactory oldFactory = Persistence.createEntityManagerFactory("OldModelIP");
        EntityManager oldEm = oldFactory.createEntityManager();
        assertNotNull(oldEm);

        List<EasyDeclaration> oldList = oldEm.createNativeQuery(DECLARATION_SQL, EasyDeclaration.class).getResultList();
        assertNotNull(oldList);
        for (EasyDeclaration old: oldList) {
            Declaration newDecl = declDao.calcDeclaration(old.getYear(), old.getMonth());
            assertEquals(newDecl.getNalog(), old.getS5(), 0.01);
        }
        //Declaration decl = declDao.calculate();
        //12-2008
    }
}
