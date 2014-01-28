package softclub.model;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import loader.base.CoreIpModelTester;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: gid
 * Date: 28.01.14
 * Time: 11:55
 */
public class PaymentDaoTest extends CoreIpModelTester {
    @ObjectUnderTest(implementation = PaymentDao.class)
    protected PaymentDao paymentDao;

    @Test
    public void testGetInputSum4Date() throws Exception {
        Double r = paymentDao.getInputSum4Date(8,2010);
        assertNotNull(r);
    }
}
