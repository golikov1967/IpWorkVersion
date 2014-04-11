package softclub.model.entities;

import by.softclub.fos.model.dao.base.BaseEntity;
import softclub.model.entities.pk.DocumentId;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@DiscriminatorValue("Document")
@Inheritance(strategy = InheritanceType.JOINED)
@Access(value = AccessType.PROPERTY)
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

}
