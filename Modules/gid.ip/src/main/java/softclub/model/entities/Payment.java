package softclub.model.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@DiscriminatorValue("Payment")
@Access(value = AccessType.PROPERTY)
public class Payment extends Document implements Serializable {

    private PayType payType;
    private Declaration declaration;
    private Date applyDate;
    private String payNote;
    private BigDecimal paySum;
    private Payer payer;
    private Integer sequenceNumber;

    public Payment() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public PayType getPayType() {
        return payType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DECLARATION_ID")
    public Declaration getDeclaration() {
        return declaration;
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

    @Column(name = "PAY_SUM")
    public BigDecimal getPaySum() {
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

    private Act act;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "ACT_DATE", referencedColumnName = "DOC_DATE"),
            @JoinColumn(name = "ACT_NUMBER", referencedColumnName = "DOC_NUMBER")
    })
    public Act getAct() {
        return act;
    }

    public void setAct(Act act) {
        this.act = act;
    }

    private Payer recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECIPIENT_UNP", referencedColumnName = "UNP")
    public Payer getRecipient() {
        return recipient;
    }

    public void setRecipient(Payer recipient) {
        this.recipient = recipient;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public void setPaySum(BigDecimal paySum) {
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
