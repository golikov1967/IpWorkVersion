package softclub.model.entities.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by gid_000 on 13.04.14.
 */
@Embeddable
public class DeclarationId implements Serializable {
    int month;

    public DeclarationId() {
    }

    public DeclarationId(int month, int year) {
        this.month = month;
        this.year = year;
    }

    @Column(name = "IMONTH", nullable =false)
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    int year;

    @Column(nullable =false, name = "IYEAR")
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeclarationId)) return false;

        DeclarationId that = (DeclarationId) o;

        if (month != that.month) return false;
        if (year != that.year) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = month;
        result = 31 * result + year;
        return result;
    }
}
