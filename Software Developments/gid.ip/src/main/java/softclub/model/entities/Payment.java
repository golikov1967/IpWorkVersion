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

    @ManyToOne(fetch = FetchType.LAZY)
    private Act act;


    @ManyToOne(fetch = FetchType.LAZY)
    private PayType payType;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "APPLY_DATE", nullable = false)
    private Date applyDate;

    private Declaration declaration;

    @Column(length = 2000, name = "PAY_NOTE")
    private String payNote;

    @Column(name = "PAY_SUM")
    private double paySum;

    @Column(name = "PAY_SUM_STRING", length = 2000)
    private String paySumString;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account payerAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account recipientAccount;

    @Column(name = "SEQUENCE_NUMBER")
    private Integer sequenceNumber;

    public Payment() {
    }

    public Account getPayerAccount() {
        return payerAccount;
    }

    public void setPayerAccount(Account payerAccount) {
        this.payerAccount = payerAccount;
    }

    public Account getRecipientAccount() {
        return recipientAccount;
    }

    public void setRecipientAccount(Account recipientAccount) {
        this.recipientAccount = recipientAccount;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getPaySumString() {
        return paySumString;
    }

    public void setPaySumString(String paySumString) {
        this.paySumString = paySumString;
    }

    public double getPaySum() {
        return paySum;
    }

    public void setPaySum(double paySum) {
        this.paySum = paySum;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getPayNote() {
        return payNote;
    }

    public void setPayNote(String payNote) {
        this.payNote = payNote;
    }

    public Act getAct() {
        return act;
    }

    public void setAct(Act act) {
        this.act = act;
    }

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = { CascadeType.ALL })
    public Declaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }
}
