package softclub.model.entities;

//~--- JDK imports ------------------------------------------------------------

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NamedQueries({ @NamedQuery(
    name  = "BussinessMan.findAll",
    query = "select o from BussinessMan o"
) })
@Table(name = "BUSSINESS_MAN")
//@SecondaryTable(name = "PERSON")
public class BussinessMan extends CommonJudicInfo implements Serializable {
//    @Column(name = "FIRST_NAME", table = "PERSON")
//    private String firstName;
//
//    @Column(name = "LAST_NAME", table = "PERSON")
//    private String lastName;
//
//    @Column(name = "MIDDLE_NAME", table = "PERSON")
//    private String middleName;
    @OneToOne
    private Person personInfo = new Person();

    public BussinessMan() {}

    public void setFirstName(String firstName) {
        personInfo.setFirstName(firstName);
    }

    public String getFirstName() {
        return personInfo.getFirstName();
    }
    
    public void setLastName(String lastName) {
        personInfo.setLastName(lastName);
    }

    public String getLastName() {
        return personInfo.getLastName();
}

    public void setMiddleName(String middleName) {
        personInfo.setMiddleName(middleName);
    }

    public String getMiddleName() {
        return personInfo.getMiddleName();
    }
}
