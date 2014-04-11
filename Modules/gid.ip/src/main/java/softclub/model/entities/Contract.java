package softclub.model.entities;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Access(value = AccessType.PROPERTY)
@DiscriminatorValue("Contract")
public class Contract extends Document implements Serializable {

    public Contract() {
    }

    //(columnDefinition = "клиент по договору")
    private Payer customer;

    @ManyToOne
    public Payer getCustomer() {
        return customer;
    }

    public void setCustomer(Payer customer) {
        this.customer = customer;
    }

    //акты по договору
    private Set<Act> actList = new HashSet<Act>(0);

    @OneToMany(mappedBy = "contract")
    public Set<Act> getPaymentList() {
        return actList;
    }

    public void setPaymentList(Set<Act> actList) {
        this.actList = actList;
    }

}
