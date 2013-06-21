package softclub.model.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "DECLARATION")
//@NamedQueries( { @NamedQuery(name = "SuperDeclaration.findAll",
//                             query = "select o from SuperDeclaration o") })

public class SuperDeclaration extends VersionedEntity<Long> {

    @Column(name = "TOTAL_INPUT_FROM_BEGIN_YEAR")
    protected double totalInputFromBeginYear = 0;

    @Column(name = "TOTAL_INPUT")
    protected double totalInput = 0;

    @Column(name = "NALOG")
    protected double nalog = 0;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "BEGIN_DATE")
    protected Date beginDate;

    public void setTotalInput(double totalInput) {
        this.totalInput = totalInput;
        this.nalog = this.totalInput / 100 * 8;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public void setTotalInputFromBeginYear(double totalInputFromBeginYear) {
        this.totalInputFromBeginYear = totalInputFromBeginYear;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public double getTotalInput() {
        return totalInput;
    }

    public void setNalog(double nalog) {
        //this.nalog = nalog;
    }

    public double getTotalInputFromBeginYear() {
        return totalInputFromBeginYear;
    }

    public double getNalog() {
        return nalog;
    }
}
