package softclub.model.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Интерфейс базовой сущности.
 *
 * @param <I> тип первичного ключа.
 * @version 1.0, 09/04/28
 * @author Вощило Юрий (vyf)
 */
@MappedSuperclass
public abstract class VersionedEntity<I extends Serializable> implements Serializable {

    @Version
    @Column(name = "VERSION", nullable = false)
    private int version = 0;

    @Id
    @Column(name = "RECORD_ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OBJECTS_SEQ")
    @SequenceGenerator(name="OBJECTS_SEQ", sequenceName="OBJECTS_ID_SEQ",
    allocationSize=1)
    private I id;

    public int getVersion() {
        return version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        final String clazz = String.format("%s@%s", getClass().getName(), Integer.toHexString(hashCode()));
        return String.format("%s[%s] v.%s", clazz, this.getId(), this.getVersion());
    }

    public I getId() {
        return id;
    }

    public void setId(I id) {
        this.id = id;
    }
}
