package softclub.model.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "DECLARATION")
//@NamedQueries( { @NamedQuery(name = "SuperDeclaration.findAll",
//                             query = "select o from SuperDeclaration o") })

public class SuperDeclaration extends VersionedEntity<Long> {

    protected Date beginDate;
    protected double totalInput = 0;
    protected double nalog = 0;
    protected double totalInputFromBeginYear = 0;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "BEGIN_DATE")
    public Date getBeginDate() {
        return beginDate;
    }

    @Column(name = "NALOG")
    public double getNalog() {
        return nalog;
    }

    @Column(name = "TOTAL_INPUT_FROM_BEGIN_YEAR")
    public double getTotalInputFromBeginYear() {
        return totalInputFromBeginYear;
    }

    @Column(name = "TOTAL_INPUT")
    public double getTotalInput() {
        return totalInput;
    }

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

    public void setNalog(double nalog) {
        //this.nalog = nalog;
    }
}
