package softclub.model.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@DiscriminatorValue("OUTPUT_PAYMENT")
@XmlRootElement(name = "ROW")
public class OutputPayment extends Payment {
    @XmlElement(name = "PAY_ACCOUNT")
    @Transient
    public String getPayerAccount() {
        Account account = getPayerAccount(payer);
        return account!=null? Long.valueOf(account.getAccountNumber()).toString(): null;
    }

    @XmlElement(name = "PAY_UNP")
    @Transient
    public String getPayerUNP() {
        return payer.getId();
    }

    @XmlElement(name = "BANK_GET_NAME")
    @Transient
    public String getRecipientBankName() {
        Bank bank = recipient.getAccounts().iterator().next().getBank();
        return bank==null? recipient.name: bank.name;
    }

    @XmlElement(name = "BANK_CODE")
    @Transient
    public String getBankCode() {
        Account account = getPayerAccount(recipient);
        Bank bank = null;
        if(account!=null){
            bank = account.getBank();
        }
        return bank==null? ((Bank)recipient).getCode(): bank.getCode();
    }

    @XmlElement(name = "BEN_NAME")
    @Transient
    public String getBeneficiaryName() {
        return recipient.name;
    }

    @XmlElement(name = "BEN_ACCOUNT")
    @Transient
    public String getBeneficiaryAccount() {
        Account account = getPayerAccount(recipient);
        return account!=null? Long.valueOf(account.getAccountNumber()).toString(): null;
    }

    @XmlElement(name = "BEN_UNP")
    @Transient
    public String getBeneficiaryUNP() {
        return recipient.getId();
    }

    @XmlElement(name = "QUEUE")
    @Transient
    public String getQUEUE() {
        return Integer.toString(payType.getQueue());
    }

    @XmlElement(name = "PAY_CODE")
    @Transient
    public Integer getPayCode() {
        return payType.getPayCode();
    }


    @XmlElement(name = "BANK_PAYER_NAME")
    @Transient
    public String getBankPayerName() {
        Account payerAccount = getPayerAccount(payer);
        return payerAccount==null? null: payerAccount.getBank().getName();
    }

    @XmlElement(name = "BANK_PAYER_CODE")
    @Transient
    public String getBankPayerCode() {
        Account payerAccount = getPayerAccount(payer);
        return payerAccount==null? ((Bank)payer).getCode(): payerAccount.getBank().getCode();
    }

    private Account getPayerAccount(Payer accPayer) {
        if(accPayer.getAccounts().size()>1){
            for(Account account: accPayer.getAccounts()){
                if(payType!=null){
                    if(payType.getId().equals(account.getPayType().getId())){
                        return account;
                    }
                }
            }
        }
        return accPayer.getAccounts().iterator().next();
    }
}
