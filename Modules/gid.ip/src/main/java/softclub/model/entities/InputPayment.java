package softclub.model.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@DiscriminatorValue("INPUT_PAYMENT")
@Access(value = AccessType.PROPERTY)
public class InputPayment extends Payment {
}
