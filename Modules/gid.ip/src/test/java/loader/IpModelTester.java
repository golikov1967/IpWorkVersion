package loader;

import loader.base.CoreIpModelTester;
import org.junit.Test;
import softclub.model.entities.*;
import softclub.model.entities.pk.DocumentId;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Created with IntelliJ IDEA.
 * User: gid
 * Date: 21.06.13
 * Time: 17:34
 */
public class IpModelTester extends CoreIpModelTester {

    public static final String SELECT_PAY_TYPES =
            "select  t.type_code, min(t.long_name), min(t.queue), min(t.pay_code) from pay_types t group by t.type_code";
    public static final String SELECT_OUT_PP =
            "select id_pp,\n" +
            "       o.doc_date,\n" +
            "       o.doc_sum,\n" +
            "       p.unp p_unp,\n" +
            "       o.bank4get,\n" +
            "       b.unp b_unp,\n" +
            "       pt.type_code,\n" +
            "       o.pay_type_text,\n" +
            "       o.oper_date,\n" +
            "       o.bank4put\n" +
            "  from out_pp o, payers p, payers b, pay_types pt\n" +
            "  where o.payer_id = p.payer_id(+)\n" +
            "  and o.beneficiary = b.payer_id(+)\n" +
            "  and o.pay_type_id = pt.pay_type_id(+)";
    public static final int IN_DOC_NUM = 0;
    public static final int IN_DOC_DATE = 1;
    public static final int IN_DOC_SUM = 2;
    public static final int IN_AKT_NUM = 3;
    public static final int IN_AKT_DATE = 4;
    public static final int IN_PAY_NOTE = 5;
    public static final int IN_CONTRACT_NUM = 6;
    public static final int IN_CONTRACT_DATE = 7;
    public static final int IN_OPER_DATE = 10;


    @Test
    /**
     * TODO: Здесь главный тест модели
     * */
    public void reloadData(){
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

        //загрузка приходных документов
        loadIn_ppDocument(oldEm, newEm);
        //загрузка субьектов
        loadPayers(oldEm, newEm);
        //загрузка типов платежа
        loadPayTypes(oldEm, newEm);
        //загрузка расходных документов
        loadOut_ppDocument(oldEm, newEm);
    }

    private void loadPayTypes(EntityManager oldEm, EntityManager newEm) {
        Query payTypesQuery = oldEm.createNativeQuery(SELECT_PAY_TYPES);
        EntityTransaction transaction = newEm.getTransaction();
        transaction.begin();
        int i = 0;
        for(Object o :payTypesQuery.getResultList()) {
            Object[] attr = (Object[]) o;
            String code = (String) attr[0];

            PayType payType = newEm.find(PayType.class, code);
            if(payType==null){
                payType = new PayType();
                payType.setId(code);
            }
            payType.setNote((String) attr[1]);
            payType.setQueue(((BigDecimal) attr[2]).intValue());
            BigDecimal payCode = (BigDecimal) attr[3];
            if(payCode!=null){
                payType.setPayCode(payCode.intValue());
            }
            newEm.merge(payType);
            LOGGER.info(payType.getId() + ":" + payType.getNote());
        }
        commit(newEm, transaction);
        LOGGER.error("загрузка Типов платежей прошла успешно, загружено " + payTypesQuery.getResultList().size() + " записей");
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
            String unp = (String) attr[0];
            String bankCode = (String) attr[3];
            Payer payer = newEm.find(Payer.class, unp);
            if(payer==null){
                if(bankCode!=null){
                    payer = new Bank();
                    ((Bank)payer).setCode(bankCode);
                } else{
                    payer = new Payer();
                }
                payer.setId(unp);
            }
            payer.setName((String) attr[1]);
            payer.getAccounts().add(findAccount((String) attr[2]));
            newEm.merge(payer);
            LOGGER.info(payer.getName() + ":" + payer.getId());
        }
        commit(newEm, transaction);
        LOGGER.error("загрузка плательщиков прошла успешно, загружено " + oldDocumentQuey.getResultList().size() + " записей");
    }

    private Account findAccount(String account) {
        return null;
    }

    private void loadOut_ppDocument(EntityManager oldEm, EntityManager newEm) {
        Query oldDocumentQuey = oldEm.createNativeQuery(SELECT_OUT_PP);
        EntityTransaction transaction = newEm.getTransaction();
        transaction.begin();
        int i = 0;
        for(Object o :oldDocumentQuey.getResultList()) {
            Object[] attr = (Object[]) o;
            // ID_PP
            final String docNumber = ((BigDecimal) attr[0]).toString();
            // DOC_DATE
            final Date docDate = getAsDate(attr[1]);
            OutputPayment pay = findDoc(newEm, docNumber, docDate, new OutputPayment());
            // DOC_SUM
            final Object docSumm = attr[2];
            if(docSumm!=null)
                pay.setPaySum(((BigDecimal) docSumm).doubleValue());
            //3 PAYER_ID
            String payerUnp = (String) attr[3];
            if(payerUnp!=null){
                pay.setPayer(newEm.find(Payer.class, payerUnp));
            }

            //4 BANK4GET
            //5 BENEFICIARY
            String benUnp = (String) attr[5];
            if(benUnp!=null){
                pay.setRecipient(newEm.find(Payer.class, benUnp));
            }

            //6 PAY_TYPE_ID
            String pTypeCode = (String) attr[6];
            if(pTypeCode!=null){
                pay.setPayType(newEm.find(PayType.class, pTypeCode));
            }
            //7 PAY_TYPE_TEXT
            pay.setPayNote((String) attr[7]);
            //8 OPER_DATE
            final Date operDate = getAsDate(attr[8]);
            pay.setApplyDate(operDate ==null? docDate: operDate);
            //9 BANK4PUT

            newEm.merge(pay);
            commit(newEm, transaction);
            transaction.begin();

        }
    }


    private void loadIn_ppDocument(EntityManager oldEm, EntityManager newEm) {
        Query oldDocumentQuey = oldEm.createNativeQuery("select * from In_pp /*where rownum<5*/ order by doc_date, doc_num");
        EntityTransaction transaction = newEm.getTransaction();
        int i = 0;
        transaction.begin();
        for(Object o :oldDocumentQuey.getResultList()) {
            Object[] attr = (Object[]) o;

            final String docNumber = (String) attr[IN_DOC_NUM];

            final Date docDate = getAsDate(attr[IN_DOC_DATE]);
            InputPayment pay = findDoc(newEm, docNumber, docDate, new InputPayment());
            //
            final Object docSumm = attr[IN_DOC_SUM];
            if(docSumm!=null)
            pay.setPaySum(((BigDecimal) docSumm).doubleValue());
            //
            Act act = findDoc(newEm, (String) attr[IN_AKT_NUM], getAsDate(attr[IN_AKT_DATE]), new Act());
            pay.setAct(act);

            //
            pay.setPayNote((String)attr[IN_PAY_NOTE]);

            if(act!=null && act.getContract() == null){
                //
                //
                Contract contract = findDoc(newEm, (String) attr[IN_CONTRACT_NUM], getAsDate(attr[IN_CONTRACT_DATE]), new Contract());
                if(contract!=null){
                    //TODO: придумать алгоритм распознавания контрагента
                    contract.setCustomer(null);
                }
                act.setContract(contract);
            }
            //
            final Date operDate = getAsDate(attr[IN_OPER_DATE]);
            pay.setApplyDate(operDate ==null? docDate: operDate);

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

            DocumentId docId = new DocumentId(docDate, docNumber);
            doc.setId(docId);
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


