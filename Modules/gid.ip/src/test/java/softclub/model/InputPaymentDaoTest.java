package softclub.model;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import loader.base.CoreIpModelTester;
import org.junit.Test;
import softclub.model.entities.Payment;
import softclub.model.entities.pk.DocumentId;

import java.sql.Date;
import java.util.Calendar;

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
        Double r = inputPaymentDao.getInputSum4Date(2011, 0, 9).doubleValue();
        assertNotNull(r);

        r = outputPaymentDao.getMinusSum4Date(1,2008).doubleValue();
        assertNotNull(r);
    }

    @Test
    public void testFindInputPay(){
        Calendar сal = Calendar.getInstance();
        //"2005-08-23";"15532"
        сal.set(2005,8 - 1,23);

        Date docDate = new Date(сal.getTime().getTime());
        Payment pay = inputPaymentDao.find(new DocumentId(
                docDate,
                "15532"
        ));
        assertNotNull(pay);
    }

    @Test
    public void testFindOutputPayment(){
        Payment pay = outputPaymentDao.findAny();
        assertNotNull(pay);
    }
}
