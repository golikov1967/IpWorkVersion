package softclub.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(
    name  = "PayType.findAll",
    query = "select o from PayType o"
) })
@Inheritance
@Table(name = "PAY_TYPE")
public class PayType extends VersionedEntity<Long> {

    /**
     * Константа определения типа платежа
     */
    @Column(length = 30)
    private String code;

    /**
     * Типовое параметризованное назначение платежа
     * Параметры:
     * {0} - NEXT_MONTH4DOC_DATE "январь 2009"
     */
    @Column(length = 2000)
    private String note;

    private int payCode;

    private int queue;

    public PayType() {}

    public String getNote() {
        return note;
    }

    public void setNote(String Note) {
        this.note = Note;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String Code) {
        this.code = Code;
    }

    public int getQueue() {
        return queue;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }

    public int getPayCode() {
        return payCode;
    }

    public void setPayCode(int payCode) {
        this.payCode = payCode;
    }
}
