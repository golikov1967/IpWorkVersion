package loader;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import loader.base.CoreIpModelTester;
import loader.entity.EasyDeclaration;
import org.junit.Test;
import softclub.model.DeclarationDao;
import softclub.model.entities.Declaration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by gid_000 on 23.01.14.
 */
public class DeclarationTester extends CoreIpModelTester {

    public static final String DECLARATION_SQL =
            "select IMONTH,IYEAR,S1,S2,S2_1,S3,S3_1,S4,S5 " +
            "from T_EASY_DECL_NEW " +
            "where IYEAR is not null and IMONTH is not null " +
            "order by IYEAR, IMONTH ";
    @ObjectUnderTest(implementation = DeclarationDao.class)
    protected DeclarationDao declDao;

    //TODO Реализовать тест сверки сумм по декларациям старой и новой моделей
    @Test
    public void declarationTest(){
        assertNotNull(declDao);
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
    }
}
