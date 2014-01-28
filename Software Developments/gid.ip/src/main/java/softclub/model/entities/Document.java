package softclub.model.entities;

import by.softclub.fos.model.dao.base.BaseEntity;
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
public class Document implements BaseEntity<DocumentId> {

    protected DocumentId id;

    public Document() {}

    @Override
    @EmbeddedId
    public DocumentId getId() {
        if(id==null){
            id = new DocumentId();
        }
        return id;
    }

    @Override
    public void setId(DocumentId documentId) {
        id = documentId;
    }

    public void setDocNumber(String docNumber) {
        getId().setDocNumber(docNumber);
    }

    public void setDocDate(Date docDate) {
        getId().setDocDate(docDate);
    }

    public String getDocNumber() {
        return getId().getDocNumber();
    }

    public Date getDocDate() {
        return getId().getDocDate();
    }

}
