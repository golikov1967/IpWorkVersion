package softclub.model.entities;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Act")
@Access(value = AccessType.PROPERTY)
public class Act extends Document implements Serializable {

    public Act() {
    }

    private Set<Payment> paymentList = new HashSet<Payment>(0);

    @OneToMany(mappedBy = "act")
    public Set<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(Set<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    private Contract contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "CONTRACT_DATE", referencedColumnName = "DOC_DATE", nullable=true, updatable=false),
            @JoinColumn(name = "CONTRACT_NUMBER", referencedColumnName = "DOC_NUMBER", nullable=true, updatable=false)
    })
    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }
}
