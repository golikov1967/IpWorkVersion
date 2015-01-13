package softclub.model.entities;

//~--- JDK imports ------------------------------------------------------------

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "BANK")
@Access(value = AccessType.PROPERTY)
public class Bank extends Payer implements Serializable {

    private String code;

    public Bank() {}

    @Column(length = 9)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
