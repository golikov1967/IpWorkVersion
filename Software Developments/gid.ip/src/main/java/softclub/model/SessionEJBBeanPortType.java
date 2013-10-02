package softclub.model;

import softclub.model.entities.*;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.rmi.Remote;
import java.util.Date;
import java.util.List;

@WebService
public interface SessionEJBBeanPortType extends Remote {
//    @WebMethod
//    public List<BussinessMan> getBussinessManFindAll();

    @Oneway
    @WebMethod
    public void parseActList();

    @WebMethod
    public Account persistAccount(Account account);

    @WebMethod
    public Account mergeAccount(Account account);

    @Oneway
    @WebMethod
    public void removeAccount(Account account);

    @WebMethod
    public List<Account> getAccountFindAll();

    @WebMethod
    public Bank persistBank(Bank bank);

    @WebMethod
    public Bank mergeBank(Bank bank);

    @Oneway
    @WebMethod
    public void removeBank(Bank bank);

    @WebMethod
    public List<Bank> getBankFindAll();

//    @WebMethod
//    public BussinessMan persistBussinessMan(BussinessMan bussinessMan);

//    @WebMethod
//    public BussinessMan mergeBussinessMan(BussinessMan bussinessMan);

//    @Oneway
//    @WebMethod
//    public void removeBussinessMan(BussinessMan bussinessMan);

    @WebMethod
    public Currency persistCurrency(Currency currency);

    @WebMethod
    public Currency mergeCurrency(Currency currency);

    @Oneway
    @WebMethod
    public void removeCurrency(Currency currency);

    @WebMethod
    public List<Currency> getCurrencyFindAll();

    @WebMethod
    public Payment persistPayment(Payment payment);

    @WebMethod
    public Payment mergePayment(Payment payment);

    @Oneway
    @WebMethod
    public void removePayment(Payment payment);

    @WebMethod
    public List<Payment> getPaymentForPeriod(Date begDay, Date endDay);

    @WebMethod
    public List<Payer> getCommonJudicInfoFindAll();

    @WebMethod
    public Payer persistCommonJudicInfo(Payer payer);

    @WebMethod
    public Payer mergeCommonJudicInfo(Payer payer);

    @Oneway
    @WebMethod
    public void removeCommonJudicInfo(Payer payer);

    @WebMethod
    public PayType persistPayType(PayType payType);

    @WebMethod
    public PayType mergePayType(PayType payType);

    @Oneway
    @WebMethod
    public void removePayType(PayType payType);

    @WebMethod
    public List<PayType> getPayTypeFindAll();

    @WebMethod
    public Contract persistContract(Contract contract);

    @WebMethod
    public Contract mergeContract(Contract contract);

    @Oneway
    @WebMethod
    public void removeContract(Contract contract);

    @WebMethod
    public List<Contract> getContractFindAll();

    @WebMethod
    public Person persistPerson(Person person);

    @WebMethod
    public Person mergePerson(Person person);

    @Oneway
    @WebMethod
    public void removePerson(Person person);

    @WebMethod
    public List<Person> getPersonFindAll();

    @WebMethod
    public Act persistAct(Act act);

    @WebMethod
    public Act mergeAct(Act act);

    @Oneway
    @WebMethod
    public void removeAct(Act act);

    @WebMethod
    public List<Act> getActFindAll();

    @WebMethod
    public Declaration addPay2Declaration(Declaration d, Payment p);

    @WebMethod
    public Declaration createDeclaration4Payment(Payment pay,
                                                 Declaration prev);
}
