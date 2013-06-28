package softclub.model;

import softclub.model.entities.*;
import softclub.model.entities.pk.DocumentId;
import softclub.utils.DateUtils;
//import softclub.utils.DateUtils;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;


@Stateless(name = "SessionEJB", mappedName = "TestWS-ModelJP-SessionEJB")
@Local(SessionEJB.class)
public class SessionEJBBean implements SessionEJB {

    private static final String NOT_PARSED = "Не разобрано:";

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    @PersistenceContext(unitName = "ModelJP")
    private EntityManager em;

    public SessionEJBBean() {
    }

    /**
     * <code>select o from BussinessMan o</code>
     */
    @Override
    public List<BussinessMan> getBussinessManFindAll() {
        Query q = em.createNamedQuery("BussinessMan.findAll");

        // if (first != null && first > 0) {
        // q.setFirstResult(first);
        // if (max != null && max > 0) {
        // q.setMaxResults(max);
        // }
        // }
        return q.getResultList();
    }

    @Override
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

        if (payment.getAct() == null) {
            Act act = Act.parseAct(payment);

            if (act != null) {
                payment.setAct(act);
                result = true;
            }
        }

        return result;
    }

    @Override
    public Account persistAccount(Account account) {
        em.persist(account);

        return account;
    }

    @Override
    public Account mergeAccount(Account account) {
        return em.merge(account);
    }

    @Override
    public void removeAccount(Account account) {
        account = em.find(Account.class, account.getId());
        em.remove(account);
    }

    /** <code>select o from Account o</code> */
    @Override
    public List<Account> getAccountFindAll() {
        CriteriaQuery<Account> query =  em.getCriteriaBuilder().createQuery(Account.class);
        return em.createQuery(query).getResultList();
    }

    @Override
    public Bank persistBank(Bank bank) {
        em.persist(bank);

        return bank;
    }

    @Override
    public Bank mergeBank(Bank bank) {
        return em.merge(bank);
    }

    @Override
    public void removeBank(Bank bank) {
        bank = em.find(Bank.class, bank.getId());
        em.remove(bank);
    }

    /** <code>select o from Bank o</code> */
    @Override
    public List<Bank> getBankFindAll() {
        return em.createNamedQuery("Bank.findAll").getResultList();
    }

    @Override
    public BussinessMan persistBussinessMan(BussinessMan bussinessMan) {
        em.persist(bussinessMan);

        return bussinessMan;
    }

    @Override
    public BussinessMan mergeBussinessMan(BussinessMan bussinessMan) {
        return em.merge(bussinessMan);
    }

    @Override
    public void removeBussinessMan(BussinessMan bussinessMan) {
        bussinessMan = em.find(BussinessMan.class, bussinessMan.getId());
        em.remove(bussinessMan);
    }

    @Override
    public Currency persistCurrency(Currency currency) {
        em.persist(currency);

        return currency;
    }

    @Override
    public Currency mergeCurrency(Currency currency) {
        return em.merge(currency);
    }

    @Override
    public void removeCurrency(Currency currency) {
        currency = em.find(Currency.class, currency.getId());
        em.remove(currency);
    }

    /** <code>select o from Currency o</code> */
    @Override
    public List<Currency> getCurrencyFindAll() {
        return em.createNamedQuery("Currency.findAll").getResultList();
    }

    @Override
    public Payment persistPayment(Payment payment) {
        em.persist(payment);

        return payment;
    }

    @Override
    public Payment mergePayment(Payment payment) {
        isChangeAct(payment);

        return em.merge(payment);
    }

    @Override
    public void removePayment(Payment payment) {
        payment = em.find(Payment.class, new DocumentId(payment.getDocDate(), payment.getDocNumber()));
        em.remove(payment);
    }

    /* Payment.forPeriod
 * */

    @Override
    public List<Payment> getPaymentForPeriod(Date begDay, Date endDay) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(GregorianCalendar.YEAR, -10);
        begDay = begDay==null? gc.getTime():begDay;
        endDay = endDay==null? new Date():endDay;
        return em.createNamedQuery("Payment.forPeriod").setParameter("begDay",
                                                                     begDay).setParameter("endDay",
                                                                                          endDay).getResultList();
    }

    /** <code>select o from CommonJudicInfo o</code> */
    @Override
    public List<CommonJudicInfo> getCommonJudicInfoFindAll() {
        return em.createNamedQuery("CommonJudicInfo.findAll").getResultList();
    }

    @Override
    public CommonJudicInfo persistCommonJudicInfo(CommonJudicInfo commonJudicInfo) {
        em.persist(commonJudicInfo);

        return commonJudicInfo;
    }

    @Override
    public CommonJudicInfo mergeCommonJudicInfo(CommonJudicInfo commonJudicInfo) {
        return em.merge(commonJudicInfo);
    }

    @Override
    public void removeCommonJudicInfo(CommonJudicInfo commonJudicInfo) {
        commonJudicInfo =
                em.find(CommonJudicInfo.class, commonJudicInfo.getId());
        em.remove(commonJudicInfo);
    }

    @Override
    public PayType persistPayType(PayType payType) {
        em.persist(payType);

        return payType;
    }

    @Override
    public PayType mergePayType(PayType payType) {
        return em.merge(payType);
    }

    @Override
    public void removePayType(PayType payType) {
        payType = em.find(PayType.class, payType.getId());
        em.remove(payType);
    }

    /** <code>select o from PayType o</code> */
    @Override
    public List<PayType> getPayTypeFindAll() {
        return em.createNamedQuery("PayType.findAll").getResultList();
    }

    @Override
    public Contract persistContract(Contract contract) {
        em.persist(contract);

        return contract;
    }

    @Override
    public Contract mergeContract(Contract contract) {
        return em.merge(contract);
    }

    @Override
    public void removeContract(Contract contract) {
        contract = em.find(Contract.class, new DocumentId(contract.getDocDate(), contract.getDocNumber()));
        em.remove(contract);
    }

    /** <code>select o from Contract o</code> */
    @Override
    public List<Contract> getContractFindAll() {
        return em.createNamedQuery("Contract.findAll").getResultList();
    }

    @Override
    public Person persistPerson(Person person) {
        em.persist(person);

        return person;
    }

    @Override
    public Person mergePerson(Person person) {
        return em.merge(person);
    }

    @Override
    public void removePerson(Person person) {
        person = em.find(Person.class, person.getId());
        em.remove(person);
    }

    /** <code>select o from Person o</code> */
    @Override
    public List<Person> getPersonFindAll() {
        return em.createNamedQuery("Person.findAll").getResultList();
    }

    @Override
    public Act persistAct(Act act) {
        em.persist(act);

        return act;
    }

    @Override
    public Act mergeAct(Act act) {
        return em.merge(act);
    }

    @Override
    public void removeAct(Act act) {
        act = em.find(Act.class, new DocumentId(act.getDocDate(), act.getDocNumber()));
        em.remove(act);
    }

    /**
     * <code>select o from Act o</code>
     */
    @Override
    public List<Act> getActFindAll() {
        return em.createNamedQuery("Act.findAll").getResultList();
    }

    @Override
    public Declaration addPay2Declaration(Declaration d, Payment p) {
        if (!d.equals(p.getDeclaration())) {
            Declaration old = p.getDeclaration();

            d.setTotalInput(d.getTotalInput() + p.getPaySum());
            if (d.getPrev() != null) {
                d.setTotalInputFromBeginYear(d.getPrev().getTotalInputFromBeginYear() +
                                             d.getPrev().getTotalInput());
            }
            if (old != null) {
                old.setTotalInput(old.getTotalInput() - p.getPaySum());
                old = em.merge(old);
            }
        }

        return d;
    }

    @Override
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
