package softclub.model.entities;

//~--- JDK imports ------------------------------------------------------------

import javax.persistence.*;
import java.util.Date;

@Entity
@NamedQueries({ @NamedQuery(
    name  = "CommonJudicInfo.findAll",
    query = "select o from CommonJudicInfo o"
) })
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "COMMON_JUDIC_INFO")
public class CommonJudicInfo extends VersionedEntity<Long> {

    @Column(length = 9)
    private String UNP;

    @Column(length = 120)
    protected String name;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "REGISTRATION_DATE")
    private Date registrationDate;

    public CommonJudicInfo() {}

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public String getUNP() {
        return UNP;
    }

    public void setUNP(String UNP) {
        this.UNP = UNP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
