package softclub.model.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@DiscriminatorValue("Payment")
@Access(value = AccessType.PROPERTY)
@XmlRootElement
public class Payment extends Document implements Serializable {

    protected PayType payType;
    private Declaration declaration;
    private Date applyDate;
    private String payNote;
    private BigDecimal paySum;
    protected Payer payer;
    private Integer sequenceNumber;

    public void setDocSumString(String docSumString) {
        this.docSumString = docSumString;
    }

    private String docSumString;

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

    @XmlElement(name = "DOC_DATE_STRING")
    @Transient
    public String getDocDateString(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(applyDate);
    }


    @XmlElement(name = "DOC_SUM_STRING")
    @Transient
    public String getDocSumString(){
        return docSumString;
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

    @XmlElement(name = "DOC_SUM")
    @Column(name = "PAY_SUM")
    public BigDecimal getPaySum() {
        return paySum;
    }


    @Column(name = "SEQUENCE_NUMBER")
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    @XmlElement(name = "PAY_TYPE_TEXT")
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

    protected Payer recipient;

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


    @Transient
    @XmlElement(name = "PAY_NAME")
    public String getPayerName() {
        return payer.name + ", РБ г.Минск ул.Руссиянова д.27 корп.2 кв.114";
    }
}
