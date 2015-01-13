package softclub.model.entities;

import by.softclub.fos.model.dao.base.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "CURRENCY")
@Access(value = AccessType.PROPERTY)
public class Currency implements BaseEntity<String> {

    private int codeISO;
    private String name;

    private String nameISO;

    public Currency() {}

    @Column(
            length = 3,
            name   = "name_ISO"
    )
    @Id
    public String getNameISO() {
        return nameISO;
    }

    public void setNameISO(String nameISO) {
        this.nameISO = nameISO;
    }

    @Column(
            length = 3,
            name   = "code_ISO"
    )
    public int getCodeISO() {
        return codeISO;
    }

    public void setCodeISO(int codeISO) {
        this.codeISO = codeISO;
    }

    @Column(
            length = 50,
            name   = "NAME"
    )
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return nameISO;
    }

    @Override
    public void setId(String newId) {
        nameISO = newId;
    }
}
