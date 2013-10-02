package softclub.model.entities;

import javax.persistence.*;


@Entity
@Inheritance
@Table(name = "ACCOUNT")
public class Account extends VersionedEntity<Long> {
    @Id
    @Column(name = "ACCOUNT_NUMBER", nullable = false)
    private long accountNumber;

    @Id
    @ManyToOne(optional = true)
    private Bank bank;

    @ManyToOne
    private Currency currency;

    @ManyToOne
    private Payer owner;

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

    public Payer getOwner() {
        return owner;
    }

    public void setOwner(Payer payer) {
        this.owner = payer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (accountNumber != account.accountNumber) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (accountNumber ^ (accountNumber >>> 32));
    }
}
