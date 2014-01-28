package softclub.model.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("OUTPUT_PAYMENT")
public class OutputPayment extends Payment {
}
