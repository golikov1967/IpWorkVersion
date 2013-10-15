package softclub.model.entities;

import javax.persistence.*;

@Entity
@NamedQueries({ @NamedQuery(
    name  = "PayType.findAll",
    query = "select o from PayType o"
) })
@Inheritance
@Table(name = "PAY_TYPE")
public class PayType extends VersionedEntity<String> {

    /**
     * Константа определения типа платежа
     */
    private String code;

    /**
     * Типовое параметризованное назначение платежа
     * Параметры:
     * {0} - NEXT_MONTH4DOC_DATE "январь 2009"
     */
    private String note;

    private int payCode;

    private int queue;

    public PayType() {}

    @Column(length = 2000)
    public String getNote() {
        return note;
    }

    @Column(length = 30, name = "CODE")
    @Id
    @Override
    public String getId() {
        return code;
    }

    public int getPayCode() {
        return payCode;
    }

    public int getQueue() {
        return queue;
    }

    public void setNote(String Note) {
        this.note = Note;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }

    public void setId(String Code) {
        this.code = Code;
    }

    public void setPayCode(int payCode) {
        this.payCode = payCode;
    }
}
