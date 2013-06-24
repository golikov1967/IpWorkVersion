package softclub.model.entities;

import softclub.model.entities.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQueries( { @NamedQuery(name = "Payment.findAll",
                             query = "select o from Payment o ORDER BY o.applyDate, o.docDate"),
                 @NamedQuery(name = "Payment.forPeriod",
                             query = "select o from Payment o where o.applyDate between :begDay and :endDay ORDER BY o.applyDate, o.docDate") }) // o.applyDate = NULL and
@Inheritance
@DiscriminatorValue("PAYMENT")
@Table(name = "PAYMENT")
public class Payment extends Document implements Serializable {

    private Act act;
    private PayType payType;
    private Declaration declaration;
    private Date applyDate;
    private String payNote;
    private double paySum;
    private String paySumString;
    private Account payerAccount;
    private Account recipientAccount;
    private Integer sequenceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    public PayType getPayType() {
        return payType;
    }

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = { CascadeType.ALL })
    public Declaration getDeclaration() {
        return declaration;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Act getAct() {
        return act;
    }

    @Temporal(value = TemporalType.DATE)
    @Column(name = "APPLY_DATE", nullable = false)
    public Date getApplyDate() {
        return applyDate;
    }

    public Payment() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Account getPayerAccount() {
        return payerAccount;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Account getRecipientAccount() {
        return recipientAccount;
    }


    @Column(name = "PAY_SUM_STRING", length = 2000)
    public String getPaySumString() {
        return paySumString;
    }

    @Column(name = "PAY_SUM")
    public double getPaySum() {
        return paySum;
    }

    @Column(name = "SEQUENCE_NUMBER")
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    @Column(length = 2000, name = "PAY_NOTE")
    public String getPayNote() {
        return payNote;
    }

    public void setAct(Act act) {
        this.act = act;
    }

    public void setPayerAccount(Account payerAccount) {
        this.payerAccount = payerAccount;
    }

    public void setRecipientAccount(Account recipientAccount) {
        this.recipientAccount = recipientAccount;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public void setPaySumString(String paySumString) {
        this.paySumString = paySumString;
    }

    public void setPaySum(double paySum) {
        this.paySum = paySum;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void setPayNote(String payNote) {
        this.payNote = payNote;
    }

    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
