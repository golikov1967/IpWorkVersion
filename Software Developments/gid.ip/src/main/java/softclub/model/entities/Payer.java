package softclub.model.entities;

//~--- JDK imports ------------------------------------------------------------

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
//@NamedQueries({ @NamedQuery(
//    name  = "Payer.findAll",
//    query = "select o from Payer o"
//) })
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "PAYER")
public class Payer extends VersionedEntity<String> {
    private String UNP;
    protected String name;
    private Date registrationDate;
    private Set<Account> accounts = new HashSet<Account>();

    @OneToMany(mappedBy = "owner")
    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Payer() {}

    @Temporal(value = TemporalType.DATE)
    @Column(name = "REGISTRATION_DATE")
    public Date getRegistrationDate() {
        return registrationDate;
    }

    @Column(length = 120)
    public String getName() {
        return name;
    }

    @Id
    @Column(length = 9)
    @Override
    public String getId() {
        return UNP;
    }

    public void setId(String UNP) {
        this.UNP = UNP;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}
