package softclub.model.entities;

import softclub.model.entities.VersionedEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
//@NamedQueries({ @NamedQuery(
//    name  = "Document.findAll",
//    query = "select o from Document o"
//) })
@DiscriminatorValue("Document")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "DOCUMENT")
public class Document extends VersionedEntity<Long> {

    protected Date docDate;
    protected String docNumber;

    public Document() {}

    @Column(
            name   = "DOC_NUMBER",
            length = 15
    )
    public String getDocNumber() {
        return docNumber;
    }

    @Temporal(value = TemporalType.DATE)
    @Column(name = "DOC_DATE")
    public Date getDocDate() {
        return docDate;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

}
