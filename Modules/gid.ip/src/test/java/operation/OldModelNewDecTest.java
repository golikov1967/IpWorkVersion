package operation;

import org.apache.log4j.Logger;
import org.junit.Test;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.Format;
import java.text.MessageFormat;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: all
 * Date: 02.02.13
 * Time: 18:17
 * To change this template use File | Settings | File Templates.
 */
public class OldModelNewDecTest {
    private static final Logger LOGGER = Logger.getLogger(OldModelNewDecTest.class);
    private static final String PERSISTENCE_UNIT_NAME = "NewModelIP";
    public static final int ABON_PLATA = 6;
    public static final int DOHOD = 7;
    public static final int KOMISS_1_PERCENT = 9;
    private static EntityManagerFactory factory, oldFactory;

    public static final BigDecimal TEHNO_MZDA = BigDecimal.valueOf(98000l);

    @Test
    public void makeApbPaymentTest(){
        oldFactory = Persistence.createEntityManagerFactory("OldModelIP");
        EntityManager oldEm = oldFactory.createEntityManager();
        assertNotNull(oldEm);

        oldEm.getTransaction().begin();

        //вычисляем тек. остаток по счету:
        BigDecimal balance = new BigDecimal("145").
                add(new BigDecimal("4200000")).
                subtract(new BigDecimal("5000")).
                subtract(new BigDecimal("5000"));

        try{
            //поискать платеж за обслуживание счета в текущем месяце
            Query query = oldEm.createNativeQuery("select id_pp, doc_date, doc_sum, payer_id, bank4get, beneficiary, pay_type_id, oper_date, bank4put, pay_type_text from out_pp o where o.pay_type_id = ? order by o.doc_date desc");
            Query insertQuery = oldEm.createNativeQuery("insert into out_pp\n" +
                    "  (id_pp, doc_date, doc_sum, payer_id, bank4get, beneficiary, pay_type_id, oper_date, bank4put, pay_type_text)\n" + //
                    "values\n" +
                    "  (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            query.setMaxResults(1);

            Calendar dbDate = Calendar.getInstance();
            dbDate.setTime(new java.util.Date());
            int currentMonth = countMonth(dbDate);


            query.setParameter(1, ABON_PLATA);
            Object[] attr = (Object[]) query.getResultList().get(0);

            Timestamp operDay = (Timestamp) attr[1];
            dbDate.setTime(operDay);
            int lastYearMonth = countMonth(dbDate);

            Calendar newOperDay = Calendar.getInstance();
            if(lastYearMonth < currentMonth){
                //newOperDay.set(Calendar.HOUR, 0);
                newOperDay.set(Calendar.MINUTE, 0);
                newOperDay.set(Calendar.SECOND, 0);
                newOperDay.set(Calendar.MILLISECOND, 0);
                LOGGER.info("в тек. месяце за счет не платили - добавляем на сумму:" + TEHNO_MZDA);
                //v_id_pp,
                insertQuery.setParameter(1,new BigDecimal("-1000"));
                // v_doc_date,
                insertQuery.setParameter(2, newOperDay);
                // v_doc_sum,
                insertQuery.setParameter(3, TEHNO_MZDA);
                // v_payer_id,
                insertQuery.setParameter(4, attr[3]);
                // v_bank4get,
                insertQuery.setParameter(5,attr[4]);
                // v_beneficiary,
                insertQuery.setParameter(6,attr[5]);
                // v_pay_type_id,
                insertQuery.setParameter(7,attr[6]);
                // v_pay_type_text,
                //insertQuery.setParameter(8,attr[7]);
                // v_oper_date,
                insertQuery.setParameter(8,attr[7]);
                // v_bank4put
                insertQuery.setParameter(9,attr[8]);
                // v_pay_type_text,
                insertQuery.setParameter(10, getPayType(oldEm, null, attr[6]));

                int expected = insertQuery.executeUpdate();
                assertEquals(expected, 1);
                balance = balance.subtract(TEHNO_MZDA);
            }

            BigDecimal secondPay = balance.divide(new BigDecimal("1.01"), 1).divide(new BigDecimal("100.0"), 1).scaleByPowerOfTen(0);
            BigDecimal firstPay = secondPay.multiply(new BigDecimal("100"));
            BigDecimal delta = balance.subtract(secondPay).subtract(firstPay);
            LOGGER.info("firstPay:"+ firstPay + " secondPay:" + secondPay + " delta:" + delta);

            if(delta.compareTo(BigDecimal.ZERO)<0){
                secondPay = secondPay.subtract(BigDecimal.ONE);
                firstPay = secondPay.multiply(new BigDecimal("100"));
                delta = balance.subtract(secondPay).subtract(firstPay);
            }

            //платежка 1% комиссии
            query.setParameter(1, KOMISS_1_PERCENT);
            attr = (Object[]) query.getResultList().get(0);
            // v_doc_date,
            insertQuery.setParameter(2, newOperDay);
            // v_doc_sum,
            insertQuery.setParameter(3, secondPay);
            // v_payer_id,
            insertQuery.setParameter(4, attr[3]);
            // v_bank4get,
            insertQuery.setParameter(5,attr[4]);
            // v_beneficiary,
            insertQuery.setParameter(6,attr[5]);
            // v_pay_type_id,
            BigDecimal payTypeId = (BigDecimal) attr[6];
            insertQuery.setParameter(7, payTypeId);
            // v_oper_date,
            insertQuery.setParameter(8,attr[7]);
            // v_bank4put
            insertQuery.setParameter(9,attr[8]);
            //v_id_pp,
            BigDecimal seq_out_pp = getSequence(oldEm, "SEQ_OUT_PP");
            insertQuery.setParameter(1, seq_out_pp);
            // v_pay_type_text,
            insertQuery.setParameter(10, getPayType(oldEm, seq_out_pp.add(BigDecimal.ONE), payTypeId));
            int expected = insertQuery.executeUpdate();
            assertEquals(expected, 1);


            //платежка дохода
            query.setParameter(1, DOHOD);
            attr = (Object[]) query.getResultList().get(0);
            // v_doc_date,
            insertQuery.setParameter(2, newOperDay);
            // v_doc_sum,
            insertQuery.setParameter(3, firstPay);
            // v_payer_id,
            insertQuery.setParameter(4, attr[3]);
            // v_bank4get,
            insertQuery.setParameter(5,attr[4]);
            // v_beneficiary,
            insertQuery.setParameter(6,attr[5]);
            // v_pay_type_id,
            payTypeId = (BigDecimal) attr[6];
            insertQuery.setParameter(7, payTypeId);
            // v_oper_date,
            insertQuery.setParameter(8,attr[7]);
            // v_bank4put
            insertQuery.setParameter(9,attr[8]);
            //v_id_pp,
            seq_out_pp = getSequence(oldEm, "SEQ_OUT_PP");
            insertQuery.setParameter(1, seq_out_pp);
            // v_pay_type_text,
            insertQuery.setParameter(10, getPayType(oldEm, prevSysMonth(), payTypeId));
            expected = insertQuery.executeUpdate();
            assertEquals(expected, 1);


            oldEm.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            oldEm.getTransaction().rollback();
        }

    }

    private String prevSysMonth() {
        return "январь 2015";
    }

    //
    private Object getPayType(EntityManager oldEm, Object docNum, Object typeId) {
        Query query = oldEm.createNativeQuery("select t.long_name from PAY_TYPES t where t.pay_type_id = ?");
        query.setParameter(1, typeId);
        String singleResult = (String) query.getSingleResult();

        String format =  MessageFormat.format(singleResult, docNum);
        return format;
    }

    private BigDecimal getSequence(EntityManager oldEm, String seqName) {
        Query query = oldEm.createNativeQuery("select "+seqName+".Nextval from dual");
        return (BigDecimal) query.getSingleResult();
    }

    private int countMonth(Calendar dbDate) {
        return dbDate.get(Calendar.YEAR)*12 + dbDate.get(Calendar.MONTH);
    }

    //@Test
    public void newTest() {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        assertNotNull(em);
    }
}
