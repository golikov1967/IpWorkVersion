package softclub.model.entities;

import javax.persistence.*;


@Entity
@Inheritance
@Table(name = "ACCOUNT")
public class Account extends VersionedEntity<Long> {

    @Column(name = "ACCOUNT_NUMBER")
    private long accountNumber;

    @ManyToOne
    private Bank bank;

    @ManyToOne
    private Currency currency;

    @ManyToOne
    private CommonJudicInfo owner;

    public Account() {}

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public CommonJudicInfo getOwner() {
        return owner;
    }

    public void setOwner(CommonJudicInfo commonJudicInfo) {
        this.owner = commonJudicInfo;
    }
}
