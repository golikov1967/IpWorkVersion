package softclub.model.entities;

import javax.persistence.*;

@Entity
@NamedQueries({ @NamedQuery(
    name  = "Person.findAll",
    query = "select o from Person o"
) })
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "PERSON")
public class Person extends VersionedEntity<Long> {

    @Column(name = "first_Name")
    private String firstName;

    @Column(name = "last_Name")
    private String lastName;

    @Column(name = "middle_Name")
    private String middleName;

    public Person() {}

    /**
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
