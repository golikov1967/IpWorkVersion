package softclub.model.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
//@NamedQueries( { @NamedQuery(name = "Payment.findAll",
//                             query = "select o from Payment o ORDER BY o.applyDate, o.docDate"),
//                 @NamedQuery(name = "Payment.forPeriod",
//                             query = "select o from Payment o where o.applyDate between :begDay and :endDay ORDER BY o.applyDate, o.docDate") }) // o.applyDate = NULL and
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
    private Payer payer;
    private Payer recipient;
    private Integer sequenceNumber;

    public Payment() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public PayType getPayType() {
        return payType;
    }

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = { CascadeType.ALL })
    @JoinColumn(name = "DECLARATION_ID")
    public Declaration getDeclaration() {
        return declaration;
    }

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = { CascadeType.ALL })
    @JoinColumns({
            @JoinColumn(name = "ACT_DATE", referencedColumnName = "DOC_DATE"),
            @JoinColumn(name = "ACT_NUMBER", referencedColumnName = "DOC_NUMBER")
    })
    public Act getAct() {
        return act;
    }

    @Temporal(value = TemporalType.DATE)
    @Column(name = "APPLY_DATE", nullable = false)
    public Date getApplyDate() {
        return applyDate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYER_UNP", referencedColumnName = "UNP")
    public Payer getPayer() {
        return payer;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECIPIENT_UNP", referencedColumnName = "UNP")
    public Payer getRecipient() {
        return recipient;
    }


    @Column(name = "PAY_SUM")
    public double getPaySum() {
        return paySum;
    }

    @Column(name = "SEQUENCE_NUMBER")
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    @Column(length = 2000, name = "PAY_NOTE")
    public String getPayNote() {
        return payNote;
    }

    public void setAct(Act act) {
        this.act = act;
    }

    public void setRecipient(Payer recipient) {
        this.recipient = recipient;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
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

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

}
