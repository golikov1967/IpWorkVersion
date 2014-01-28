package softclub.model.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@DiscriminatorValue("INPUT_PAYMENT")
public class InputPayment extends Payment {
}
