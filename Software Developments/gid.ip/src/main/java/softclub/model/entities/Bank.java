package softclub.model.entities;

//~--- JDK imports ------------------------------------------------------------

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NamedQueries({ @NamedQuery(
    name  = "Bank.findAll",
    query = "select o from Bank o"
) })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "BANK")
public class Bank extends Payer implements Serializable {

    @Column(length = 9)
    private String code;

    public Bank() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
