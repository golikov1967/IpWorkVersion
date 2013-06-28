package softclub.model.entities.pk;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: gid
 * Date: 28.06.13
 * Time: 8:39
 */
public class DocumentId {
    protected Date docDate;
    protected String docNumber;

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
