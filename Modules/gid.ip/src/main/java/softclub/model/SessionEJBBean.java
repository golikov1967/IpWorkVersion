package softclub.model;

import softclub.model.entities.Account;
import softclub.model.entities.Act;
import softclub.model.entities.Bank;
import softclub.model.entities.Contract;
import softclub.model.entities.Currency;
import softclub.model.entities.Declaration;
import softclub.model.entities.PayType;
import softclub.model.entities.Payer;
import softclub.model.entities.Payment;
import softclub.model.entities.Person;
import softclub.utils.DateUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.logging.Logger;

//import softclub.utils.DateUtils;



@Stateless(name = "SessionEJB", mappedName = "TestWS-ModelJP-SessionEJB")
public class SessionEJBBean{

    private static final String NOT_PARSED = "Не разобрано:";

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    @PersistenceContext(unitName = "NewModelIP")
    private EntityManager em;

    public SessionEJBBean() {
    }

//    /**
//     * <code>select o from BussinessMan o</code>
//     */
//    
//    public List<BussinessMan> getBussinessManFindAll() {
//        Query q = em.createNamedQuery("BussinessMan.findAll");
//
//        // if (first != null && first > 0) {
//        // q.setFirstResult(first);
//        // if (max != null && max > 0) {
//        // q.setMaxResults(max);
//        // }
//        // }
//        return q.getResultList();
//    }

    
    public void parseActList() {
        LOGGER.info("тотальный разбор реквизитов документов - отключен. Для этих целей используйте функцию mergePayment(payment);");

//        List<Payment> pl = _getPaymentFindAll();
//        String notParsed = NOT_PARSED;
//
//        LOGGER.info("СТАРТ");
//
//        for (Payment payment : pl) {
//            if (isChangeAct(payment)) {
//                mergePayment(payment);
//            }
//        }
//
//        LOGGER.info("СТОП");
    }

    private boolean isChangeAct(Payment payment) {
        boolean result = false;

//        if (payment.getAct() == null) {
//            Act act = RegexpUtil.parseAct(payment);
//
//            if (act != null) {
//                payment.setAct(act);
//                result = true;
//            }
//        }

        return result;
    }

    
    public Account persistAccount(Account account) {
        em.persist(account);

        return account;
    }

    
    public Account mergeAccount(Account account) {
        return em.merge(account);
    }

    
    public void removeAccount(Account account) {
        account = em.find(Account.class, account.getId());
        em.remove(account);
    }

    /** <code>select o from Account o</code> */
    
    public List<Account> getAccountFindAll() {
        CriteriaQuery<Account> query =  em.getCriteriaBuilder().createQuery(Account.class);
        return em.createQuery(query).getResultList();
    }

    
    public Bank persistBank(Bank bank) {
        em.persist(bank);

        return bank;
    }

    
    public Bank mergeBank(Bank bank) {
        return em.merge(bank);
    }

    
    public void removeBank(Bank bank) {
        bank = em.find(Bank.class, bank.getId());
        em.remove(bank);
    }

    /** <code>select o from Bank o</code> */
//    
//    public List<Bank> getBankFindAll() {
//        return em.createNamedQuery("Bank.findAll").getResultList();
//    }

//    
//    public BussinessMan persistBussinessMan(BussinessMan bussinessMan) {
//        em.persist(bussinessMan);
//
//        return bussinessMan;
//    }

//    
//    public BussinessMan mergeBussinessMan(BussinessMan bussinessMan) {
//        return em.merge(bussinessMan);
//    }

//    
//    public void removeBussinessMan(BussinessMan bussinessMan) {
//        bussinessMan = em.find(BussinessMan.class, bussinessMan.getId());
//        em.remove(bussinessMan);
//    }

    
    public Currency persistCurrency(Currency currency) {
        em.persist(currency);

        return currency;
    }

    
    public Currency mergeCurrency(Currency currency) {
        return em.merge(currency);
    }

    
    public void removeCurrency(Currency currency) {
        currency = em.find(Currency.class, currency.getId());
        em.remove(currency);
    }

    /** <code>select o from Currency o</code> */
//    
//    public List<Currency> getCurrencyFindAll() {
//        return em.createNamedQuery("Currency.findAll").getResultList();
//    }

    
    public Payment persistPayment(Payment payment) {
        em.persist(payment);

        return payment;
    }

    
    public Payment mergePayment(Payment payment) {
        isChangeAct(payment);

        return em.merge(payment);
    }

    
    public void removePayment(Payment payment) {
        payment = em.find(Payment.class, payment.getId());
        em.remove(payment);
    }

    /* Payment.forPeriod
 * */

//    
//    public List<Payment> getPaymentForPeriod(Date begDay, Date endDay) {
//        GregorianCalendar gc = new GregorianCalendar();
//        gc.add(GregorianCalendar.YEAR, -10);
//        begDay = begDay==null? gc.getTime():begDay;
//        endDay = endDay==null? new Date():endDay;
//        return em.createNamedQuery("Payment.forPeriod").setParameter("begDay",
//                                                                     begDay).setParameter("endDay",
//                                                                                          endDay).getResultList();
//    }

    /** <code>select o from Payer o</code> */
//    
//    public List<Payer> getCommonJudicInfoFindAll() {
//        return em.createNamedQuery("Payer.findAll").getResultList();
//    }

    
    public Payer persistCommonJudicInfo(Payer payer) {
        em.persist(payer);

        return payer;
    }

    
    public Payer mergeCommonJudicInfo(Payer payer) {
        return em.merge(payer);
    }

    
    public void removeCommonJudicInfo(Payer payer) {
        payer =
                em.find(Payer.class, payer.getId());
        em.remove(payer);
    }

    
    public PayType persistPayType(PayType payType) {
        em.persist(payType);

        return payType;
    }

    
    public PayType mergePayType(PayType payType) {
        return em.merge(payType);
    }

    
    public void removePayType(PayType payType) {
        payType = em.find(PayType.class, payType.getId());
        em.remove(payType);
    }

    /** <code>select o from PayType o</code> */
//    
//    public List<PayType> getPayTypeFindAll() {
//        return em.createNamedQuery("PayType.findAll").getResultList();
//    }

    
    public Contract persistContract(Contract contract) {
        em.persist(contract);

        return contract;
    }

    
    public Contract mergeContract(Contract contract) {
        return em.merge(contract);
    }

    
    public void removeContract(Contract contract) {
        contract = em.find(Contract.class, contract.getId());
        em.remove(contract);
    }

    /** <code>select o from Contract o</code> */
//    
//    public List<Contract> getContractFindAll() {
//        return em.createNamedQuery("Contract.findAll").getResultList();
//    }

    
    public Person persistPerson(Person person) {
        em.persist(person);

        return person;
    }

    
    public Person mergePerson(Person person) {
        return em.merge(person);
    }

    
    public void removePerson(Person person) {
        person = em.find(Person.class, person.getId());
        em.remove(person);
    }

    /** <code>select o from Person o</code> */
//    
//    public List<Person> getPersonFindAll() {
//        return em.createNamedQuery("Person.findAll").getResultList();
//    }

    
    public Act persistAct(Act act) {
        em.persist(act);

        return act;
    }

    public Act mergeAct(Act act) {
        return em.merge(act);
    }

    public void removeAct(Act act) {
        act = em.find(Act.class, act.getId());
        em.remove(act);
    }

    /**
     * <code>select o from Act o</code>
     */
//    
//    public List<Act> getActFindAll() {
//        return em.createNamedQuery("Act.findAll").getResultList();
//    }

    public Declaration addPay2Declaration(Declaration d, Payment p) {
        if (!d.equals(p.getDeclaration())) {
            Declaration old = p.getDeclaration();

            d.setS2(d.getS2() + p.getPaySum().doubleValue());
            if (d.getPrev() != null) {
                d.setTotalInputYear(d.getPrev().getTotalInputYear() +
                                             d.getPrev().getS2());
            }
            if (old != null) {
                old.setS2(old.getS2() - p.getPaySum().doubleValue());
                old = em.merge(old);
            }
        }

        return d;
    }

    public Declaration createDeclaration4Payment(Payment pay,
                                                 Declaration prev) {
        final Declaration declaration = new Declaration();
        declaration.setPrev(prev);
        declaration.setBeginDate(DateUtils.getFirstDayOfMounth(pay.getApplyDate()));

        if (prev == null || prev.getQuartal() == null ||
            DateUtils.isLastQuartalMounth(prev.getBeginDate())) {
            declaration.setQuartal(new Declaration());
            declaration.getQuartal().setBeginDate(declaration.getBeginDate());
            ((Declaration)declaration.getQuartal()).setPrev(prev == null ? null :
                                             prev.getQuartal());
        } else {
            declaration.setQuartal(prev.getQuartal());
        }
        return declaration;
    }
}
