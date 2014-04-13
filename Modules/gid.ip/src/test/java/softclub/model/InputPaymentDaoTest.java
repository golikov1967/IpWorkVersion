package softclub.model;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import loader.base.CoreIpModelTester;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: gid
 * Date: 28.01.14
 * Time: 11:55
 */
public class InputPaymentDaoTest extends CoreIpModelTester {
    @ObjectUnderTest(implementation = InputPaymentDao.class)
    protected InputPaymentDao inputPaymentDao;

    @ObjectUnderTest(implementation = OutputPaymentDao.class)
    protected OutputPaymentDao outputPaymentDao;

    @Test
    public void testGetInputSum4Date() throws Exception {
        Double r = inputPaymentDao.getInputSum4Date(2008, 0, 1).doubleValue();
        assertNotNull(r);

        r = outputPaymentDao.getMinusSum4Date(1,2008).doubleValue();
        assertNotNull(r);
    }
}
