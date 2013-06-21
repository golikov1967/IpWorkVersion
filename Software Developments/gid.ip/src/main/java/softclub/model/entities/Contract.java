package softclub.model.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NamedQueries({
  @NamedQuery(name = "Contract.findAll", query = "select o from Contract o")
})
@Inheritance
@Table(name = "CONTRACT")
public class Contract extends Document implements Serializable {

    @ManyToOne
    //(columnDefinition = "клиент по договору")
    private CommonJudicInfo customer;

    public Contract() {
    }


    public CommonJudicInfo getCustomer() {
        return customer;
    }

    public void setCustomer(CommonJudicInfo customer) {
        this.customer = customer;
    }
}
