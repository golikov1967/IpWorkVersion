package loader;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;
import de.akquinet.jbosscc.needle.junit.NeedleRule;
import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import softclub.model.SessionEJB;
import softclub.model.SessionEJBBean;
import softclub.model.entities.Act;
import softclub.model.entities.Contract;
import softclub.model.entities.Document;
import softclub.model.entities.Payment;
import softclub.model.entities.pk.DocumentId;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


/**
 * Created with IntelliJ IDEA.
 * User: gid
 * Date: 21.06.13
 * Time: 17:34
 */
public class Loader {
//    Logger LOGGER = Logger.getRootLogger();
    Logger LOGGER = Logger.getLogger("loader.Loader");

    @Rule
    public DatabaseRule databaseRule = new DatabaseRule("NewModelIP");

    @Rule
    public NeedleRule needleRule = new NeedleRule(databaseRule);

    @ObjectUnderTest(implementation = SessionEJBBean.class)
    private SessionEJB newModel;

    @Inject
    private javax.persistence.EntityManager newEm;

    @Rule
    public DatabaseRule oldDatabaseRule = new DatabaseRule("OldModelIP");


    @Rule
    public NeedleRule oldNeedleRule = new NeedleRule(oldDatabaseRule);

    @Test
    public void reloadData(){
        EntityManager oldEm = oldDatabaseRule.getEntityManager();
        //EntityManager newEm = databaseRule.getEntityManager();

        assertNotNull(oldEm);
        assertNotNull(newModel);
        assertNotNull(newEm);

        Query oldQ = oldEm.createNativeQuery("select USER from dual");
        String user = (String) oldQ.getSingleResult();
        LOGGER.error("проверка прошла успешно user=" + user);

        Query newQ = newEm.createNativeQuery("select USER from dual");
        user = (String) newQ.getSingleResult();
        LOGGER.error("проверка прошла успешно user=" + user);

        addTestDocument(oldEm, newEm);
    }


    private void addTestDocument(EntityManager oldEm, EntityManager newEm) {
        Query oldDocumentQuey = oldEm.createNativeQuery("select * from In_pp where rownum<5 order by doc_date, doc_number");
        EntityTransaction transaction = newEm.getTransaction();
        transaction.begin();
        int i = 0;
        for(Object o :oldDocumentQuey.getResultList()) {
            Object[] attr = (Object[]) o;
            final String docNumber = (String) attr[0];
            final Date docDate = getAsDate(attr[1]);
            Payment pay = findDoc(newEm, docNumber, docDate, new Payment());
            // DOC_NUM
            pay.setDocNumber(docNumber);
            // DOC_DATE
            pay.setDocDate(docDate);
            // DOC_SUM
            final Object docSumm = attr[2];
            if(docSumm!=null)
            pay.setPaySum(((BigDecimal) docSumm).doubleValue());
            // AKT_NUM
            Act act = findDoc(newEm, (String) attr[3], getAsDate(attr[4]), new Act());
            pay.setAct(act);

            //TODO: PAY_NOTE
            pay.setPayNote((String)attr[5]);

            if(act!=null && act.getContract() == null){
                //TODO: CONTRACT_NUM
                //TODO: CONTRACT_DATE
                act.setContract(findDoc(newEm, (String) attr[6], getAsDate(attr[7]), new Contract()));
            }
            // REC_ID
            //TODO: LETTER_DATE
            //TODO: OPER_DATE
            final Date operDate = getAsDate(attr[10]);
            pay.setApplyDate(operDate ==null? docDate: operDate);
//            document.ge

            newEm.merge(pay);
//            if(i++>100){
                commit(newEm, transaction);
                transaction.begin();
                i=0;
//            }

        }

//        Document document = new Document();
//        document.setDocNumber("1");
        commit(newEm, transaction);
        LOGGER.error("загрузка входящих платежек прошла успешно, загружено " + oldDocumentQuey.getResultList().size() + " записей");
    }

    private void commit(EntityManager newEm, EntityTransaction transaction) {
        newEm.flush();
        transaction.commit();
    }

    private <T extends Document> T findDoc(EntityManager newEm, String docNumber, Date docDate, T newDoc) {
        T doc = null;
        if(docNumber!=null || docDate!=null){
            doc = (T) newEm.find(Document.class, new DocumentId(docDate, docNumber));
            doc = (doc==null? newDoc: doc);
            doc.setDocNumber(docNumber);
            doc.setDocDate(docDate);
        }
        return doc;
    }

//    private Payment findPayment(EntityManager newEm, String docNumber, Date docDate) {
//        final Payment payment = (Payment) newEm.find(Document.class, new DocumentId(docDate, docNumber));
//        return payment==null? new Payment(): payment;
//    }

    private Date getAsDate(Object o) {
        return (o!=null)? new Date(((java.sql.Timestamp) o) .getTime()): null;
    }
}
