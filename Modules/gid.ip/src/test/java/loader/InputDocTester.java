package loader;

import de.akquinet.jbosscc.needle.annotation.InjectIntoMany;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import loader.base.CoreIpModelTester;
import org.junit.Test;
import softclub.model.InputPaymentDao;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gid_000 on 23.01.14.
 */
public class InputDocTester extends CoreIpModelTester {
    protected String nDoc;
    protected Date payDate;
    protected double paySumm;
    protected String nContract;
    protected Date contractDate;
    protected String actNumber;
    protected Date actDate;

    @InjectIntoMany
    @ObjectUnderTest
    InputPaymentDao inputPaymentDao;

    public static final String DECLARATION_SQL =
            "select IMONTH,IYEAR,S1,S2,S2_1,S3,S3_1,S4,S5 " +
            "from T_EASY_DECL_NEW " +
            "where IYEAR is not null and IMONTH is not null " +
            "order by IYEAR, IMONTH ";

    @Test
    public void fromSoftclubSingle() throws ParseException{
        nDoc = "NOT_NUMBERED";
        payDate = parseDate("01/01/2999");
        paySumm = 1550400d;
        nContract = "Р15/1";
        contractDate = parseDate("03/01/2014");
        actNumber = "14/3-3";
        actDate = parseDate("03/03/2014");
        fromSoftclub();
    }


    @Test
    public void fromSoftclub() throws ParseException {
        createOldInputDocFromSoftclub(nDoc, payDate, paySumm, nContract, contractDate, actNumber, actDate);
        createNewInputDocFromSoftclub(nDoc, payDate, paySumm, nContract, contractDate, actNumber, actDate);
    }

    private void createNewInputDocFromSoftclub(String nDoc, Date payDate, double paySumm, String nContract, Date contractDate, String actNumber, Date actDate) throws ParseException {
        inputPaymentDao.fromSoftclub(nDoc, payDate, paySumm, nContract, contractDate, actNumber, actDate);
    }

    private void createOldInputDocFromSoftclub(String nDoc, Date payDate, double paySumm, String nContract, Date contractDate, String actNumber, Date actDate) {
        connectTest();
        EntityManager oldEm = oldDatabaseRule.getEntityManager();
        Query query = oldEm.createNativeQuery("begin\n" +
                "  -- Call the procedure\n" +
                "  gid_pack.decodestring('ПРЕДПРИНИМАТЕЛЬ ГОЛИКОВ ИГОРЬ ДМИТРИЕВИЧ РБ\n" +
                "№ пп: ' || ? || ' Дата перечисления: ' ||\n" +
                "                        TO_CHAR(?, 'DD/MM/YYYY') || '\n" +
                "Сумма: ' || ? || '=\tОПЛАТА СОГЛАСНО ДОГ. ' ||\n" +
                "                        ? || ' ОТ ' ||\n" +
                "                        to_char(?, 'DD/MM/YYYY') ||\n" +
                "                        ' ЗА ТЕСТИРОВАНИЕ ПО СОГЛАСНО АКТУ ' || ? || ' ОТ ' ||\n" +
                "                        to_char(?, 'DD/MM/YYYY') ||\n" +
                "                        'Г. ЦЕНЫ СОГЛАСНО П.3.3 ДОГОВОРА. БЕЗ НДС. ');\n" +
                "end;");
//        "N_Doc"
        query.setParameter(1, nDoc);
//        "payDate"
        query.setParameter(2, payDate);
//        "paySumm"
        query.setParameter(3, paySumm);
//        "n_contract"
        query.setParameter(4, nContract);
//        "contractDate"
        query.setParameter(5, contractDate);
//        "actNumber"
        query.setParameter(6, actNumber);
//        "actDate"
        query.setParameter(7, actDate);

        oldEm.getTransaction().begin();
        query.executeUpdate();
        oldEm.getTransaction().commit();
    }

    protected Date parseDate(String sDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.parse(sDate);
    }
}
