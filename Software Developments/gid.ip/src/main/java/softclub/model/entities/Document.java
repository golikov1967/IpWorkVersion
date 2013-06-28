package softclub.model.entities;

import softclub.model.entities.VersionedEntity;
import softclub.model.entities.pk.DocumentId;

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
@IdClass(DocumentId.class)
public class Document {

    protected Date docDate;
    protected String docNumber;

    public Document() {}
    @Id
    @Column(name   = "DOC_NUMBER", length = 15)
    public String getDocNumber() {
        return docNumber;
    }

    @Id
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document)) return false;

        Document document = (Document) o;

        if (!docDate.equals(document.docDate)) return false;
        if (!docNumber.equals(document.docNumber)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = docDate.hashCode();
        result = 31 * result + docNumber.hashCode();
        return result;
    }
}
