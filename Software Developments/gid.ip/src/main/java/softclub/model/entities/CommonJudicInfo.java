package softclub.model.entities;

//~--- JDK imports ------------------------------------------------------------

import javax.persistence.*;
import java.util.Date;

@Entity
//@NamedQueries({ @NamedQuery(
//    name  = "CommonJudicInfo.findAll",
//    query = "select o from CommonJudicInfo o"
//) })
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "COMMON_JUDIC_INFO")
public class CommonJudicInfo extends VersionedEntity<Long> {
    private String UNP;
    protected String name;
    private Date registrationDate;

    public CommonJudicInfo() {}

    @Temporal(value = TemporalType.DATE)
    @Column(name = "REGISTRATION_DATE")
    public Date getRegistrationDate() {
        return registrationDate;
    }

    @Column(length = 120)
    public String getName() {
        return name;
    }

    @Column(length = 9)
    public String getUNP() {
        return UNP;
    }

    public void setUNP(String UNP) {
        this.UNP = UNP;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}
