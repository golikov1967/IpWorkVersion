package softclub.model;

import softclub.model.entities.*;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: all
 * Date: 04.02.13
 * Time: 22:00
 * To change this template use File | Settings | File Templates.
 */
public interface SessionEJB {
    //List<BussinessMan> getBussinessManFindAll();

    void parseActList();

    Account persistAccount(Account account);

    Account mergeAccount(Account account);

    void removeAccount(Account account);

    List<Account> getAccountFindAll();

    Bank persistBank(Bank bank);

    Bank mergeBank(Bank bank);

    void removeBank(Bank bank);

    List<Bank> getBankFindAll();

    //BussinessMan persistBussinessMan(BussinessMan bussinessMan);

    //BussinessMan mergeBussinessMan(BussinessMan bussinessMan);

    //void removeBussinessMan(BussinessMan bussinessMan);

    Currency persistCurrency(Currency currency);

    Currency mergeCurrency(Currency currency);

    void removeCurrency(Currency currency);

    List<Currency> getCurrencyFindAll();

    Payment persistPayment(Payment payment);

    Payment mergePayment(Payment payment);

    void removePayment(Payment payment);

    List<Payment> getPaymentForPeriod(Date begDay, Date endDay);

    List<Payer> getCommonJudicInfoFindAll();

    Payer persistCommonJudicInfo(Payer payer);

    Payer mergeCommonJudicInfo(Payer payer);

    void removeCommonJudicInfo(Payer payer);

    PayType persistPayType(PayType payType);

    PayType mergePayType(PayType payType);

    void removePayType(PayType payType);

    List<PayType> getPayTypeFindAll();

    Contract persistContract(Contract contract);

    Contract mergeContract(Contract contract);

    void removeContract(Contract contract);

    List<Contract> getContractFindAll();

    Person persistPerson(Person person);

    Person mergePerson(Person person);

    void removePerson(Person person);

    List<Person> getPersonFindAll();

    Act persistAct(Act act);

    Act mergeAct(Act act);

    void removeAct(Act act);

    List<Act> getActFindAll();

    Declaration addPay2Declaration(Declaration d, Payment p);

    Declaration createDeclaration4Payment(Payment pay,
                                          Declaration prev);
}
