package softclub.model.entities.pk;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: gid
 * Date: 28.06.13
 * Time: 8:39
 */
@Embeddable
public class DocumentId implements Serializable {
    //@Id
    @Temporal(value = TemporalType.DATE)
    @Column(name = "DOC_DATE")
    protected Date docDate;

    //@Id
    @Column(name   = "DOC_NUMBER", length = 15)
    protected String docNumber;

    public String getDocNumber() {
        return docNumber;
    }

    @Temporal(value = TemporalType.DATE)
    public Date getDocDate() {
        return docDate;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    public DocumentId() {}

    public DocumentId(Date docDate, String docNumber) {
        this.docDate = docDate;
        this.docNumber = docNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DocumentId)) return false;

        DocumentId that = (DocumentId) o;

        if (!docDate.equals(that.docDate)) return false;
        if (!docNumber.equals(that.docNumber)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = docDate.hashCode();
        result = 31 * result + docNumber.hashCode();
        return result;
    }
}
