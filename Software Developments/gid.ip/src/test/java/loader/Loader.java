package loader;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;
import de.akquinet.jbosscc.needle.junit.NeedleRule;
import loader.base.LoaderCore;
import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import softclub.model.SessionEJB;
import softclub.model.SessionEJBBean;
import softclub.model.entities.*;
import softclub.model.entities.pk.DocumentId;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


/**
 * Created with IntelliJ IDEA.
 * User: gid
 * Date: 21.06.13
 * Time: 17:34
 */
public class Loader extends LoaderCore {


    @Test
    public void reloadData(){
        EntityManager oldEm = oldDatabaseRule.getEntityManager();
        //EntityManager newEm = newDatabaseRule.getEntityManager();

        assertNotNull(oldEm);
        assertNotNull(newModel);
        assertNotNull(newEm);

        Query oldQ = oldEm.createNativeQuery("select USER from dual");
        String user = (String) oldQ.getSingleResult();
        LOGGER.error("проверка прошла успешно user=" + user);

        Query newQ = newEm.createNativeQuery("select USER from dual");
        user = (String) newQ.getSingleResult();
        LOGGER.error("проверка прошла успешно user=" + user);

        //загрузка приходных документов
        //loadIn_ppDocument(oldEm, newEm);
        //загрузка субьектов
        loadPayers(oldEm, newEm);
        //загрузка расходных документов
        //loadOut_ppDocument(oldEm, newEm);
    }

    private void loadPayers(EntityManager oldEm, EntityManager newEm) {
        Query oldDocumentQuey = oldEm.createNativeQuery(
                "select p.unp, p.name, p.bank_account, b.bank_code\n" +
                " from payers p, banks b where p.payer_id = b.bank_id(+)");
        EntityTransaction transaction = newEm.getTransaction();
        transaction.begin();
        int i = 0;
        for(Object o :oldDocumentQuey.getResultList()) {
            Object[] attr = (Object[]) o;
            String bankCode = (String) attr[3];
            final Payer payer;
            if(bankCode==null){
                payer = new Bank();
                ((Bank)payer).setCode(bankCode);
            } else{
                payer = new Payer();
            }
            payer.setId((String) attr[0]);
            payer.setName((String) attr[1]);
            payer.getAccounts().add(findAccount((String) attr[2]));
            newEm.merge(payer);
            LOGGER.info(payer.getName() + ":" + payer.getId());
        }
    }

    private Account findAccount(String account) {
        return null;
    }

    private void loadOut_ppDocument(EntityManager oldEm, EntityManager newEm) {
        Query oldDocumentQuey = oldEm.createNativeQuery("select * from Out_pp /*where rownum<5*/ order by doc_date, id_pp");
        EntityTransaction transaction = newEm.getTransaction();
        transaction.begin();
        int i = 0;
        for(Object o :oldDocumentQuey.getResultList()) {
            Object[] attr = (Object[]) o;
            // ID_PP
            final String docNumber = ((BigDecimal) attr[0]).toString();
            // DOC_DATE
            final Date docDate = getAsDate(attr[1]);
            Payment pay = findDoc(newEm, docNumber, docDate, new Payment());
            // DOC_SUM
            final Object docSumm = attr[2];
            if(docSumm!=null)
                pay.setPaySum(((BigDecimal) docSumm).doubleValue());
            //3 PAYER_ID
            //4 BANK4GET
            //5 BENEFICIARY
            //6 PAY_TYPE_ID
            //7 PAY_TYPE_TEXT
            //8 OPER_DATE
            final Date operDate = getAsDate(attr[8]);
            pay.setApplyDate(operDate ==null? docDate: operDate);
            //9 BANK4PUT
        }
    }


    private void loadIn_ppDocument(EntityManager oldEm, EntityManager newEm) {
        Query oldDocumentQuey = oldEm.createNativeQuery("select * from In_pp /*where rownum<5*/ order by doc_date, doc_num");
        EntityTransaction transaction = newEm.getTransaction();
        transaction.begin();
        int i = 0;
        for(Object o :oldDocumentQuey.getResultList()) {
            Object[] attr = (Object[]) o;
            // DOC_NUM
            final String docNumber = (String) attr[0];
            // DOC_DATE
            final Date docDate = getAsDate(attr[1]);
            Payment pay = findDoc(newEm, docNumber, docDate, new Payment());
            // DOC_SUM
            final Object docSumm = attr[2];
            if(docSumm!=null)
            pay.setPaySum(((BigDecimal) docSumm).doubleValue());
            // AKT_NUM
            Act act = findDoc(newEm, (String) attr[3], getAsDate(attr[4]), new Act());
            pay.setAct(act);

            //PAY_NOTE
            pay.setPayNote((String)attr[5]);

            if(act!=null && act.getContract() == null){
                //CONTRACT_NUM
                //CONTRACT_DATE
                act.setContract(findDoc(newEm, (String) attr[6], getAsDate(attr[7]), new Contract()));
            }
            // REC_ID
            //TODO: LETTER_DATE
            //OPER_DATE
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
