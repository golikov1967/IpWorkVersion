package softclub.model.entities;

import javax.persistence.*;

@Entity
@NamedQueries({ @NamedQuery(
    name  = "Currency.findAll",
    query = "select o from Currency o"
) })
@Inheritance
@Table(name = "CURRENCY")
public class Currency extends VersionedEntity<Long> {

    @Column(
        length = 3,
        name   = "code_ISO"
    )
    private int codeISO;

    @Column(
        length = 50,
        name   = "NAME"
    )
    private String name;

    @Column(
        length = 3,
        name   = "name_ISO"
    )
    private String nameISO;

    public Currency() {}

    public String getNameISO() {
        return nameISO;
    }

    public void setNameISO(String nameISO) {
        this.nameISO = nameISO;
    }

    public int getCodeISO() {
        return codeISO;
    }

    public void setCodeISO(int codeISO) {
        this.codeISO = codeISO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
