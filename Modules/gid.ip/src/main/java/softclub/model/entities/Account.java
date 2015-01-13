package softclub.model.entities;

import by.softclub.fos.model.dao.base.BaseEntity;

import javax.persistence.*;


@Entity
@Inheritance
@Table(name = "ACCOUNT")
@Access(value = AccessType.PROPERTY)
public class Account implements BaseEntity<Long> {
    private long accountNumber;
    private Currency currency;

    public Account() {}

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    private Bank bank;

    @ManyToOne(optional = true, cascade = {CascadeType.ALL})
    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @ManyToOne
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

//    private Payer owner;
//    @ManyToOne
//    public Payer getOwner() {
//        return owner;
//    }
//
//    public void setOwner(Payer payer) {
//        this.owner = payer;
//    }

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

    @Override
    @Id
    @Column(name = "ACCOUNT_NUMBER", nullable = false)
    public Long getId() {
        return accountNumber;
    }

    @Override
    public void setId(Long newId) {
        accountNumber = newId;
    }
}
