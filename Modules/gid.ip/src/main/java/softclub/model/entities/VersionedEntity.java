package softclub.model.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Интерфейс базовой сущности.
 *
 * @param <I> тип первичного ключа.
 * @version 1.0, 09/04/28
 * @author Голиков Игорь (gid)
 */
@MappedSuperclass
@Access(value = AccessType.PROPERTY)
public abstract class VersionedEntity<I> implements Serializable {

    private int version = 0;
    private I id;

    @Id
    @Column(name = "RECORD_ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OBJECTS_SEQ")
    @SequenceGenerator(name="OBJECTS_SEQ", sequenceName="OBJECTS_ID_SEQ", allocationSize=1)
    public I getId() {
        return id;
    }

    @Version
    @Column(name = "VERSION", nullable = false)
    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        final String clazz = String.format("%s@%s", getClass().getName(), Integer.toHexString(hashCode()));
        return String.format("%s[%s] v.%s", clazz, this.getId(), this.getVersion());
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public void setId(I id) {
        this.id = id;
    }
}
