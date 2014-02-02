package softclub.model.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "DECLARATION")
//@NamedQueries( { @NamedQuery(name = "SuperDeclaration.findAll",
//                             query = "select o from SuperDeclaration o") })

public class SuperDeclaration extends VersionedEntity<Long> {

    protected Date beginDate;
    private double totalInputYear;
    private Double s2;
    private Double s2_1;
    private double s3;
    private double s4;

    @Column(name = "NALOG")
    private double nalog;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "BEGIN_DATE")
    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public void setTotalInputYear(double s1) {
        this.totalInputYear = s1;
    }

    @Column(name = "TOTAL_INPUT_FROM_BEGIN_YEAR")
    public double getTotalInputYear() {
        return totalInputYear;
    }

    public void setS2(Double s2) {
        this.s2 = s2;
    }

    @Column(name = "TOTAL_INPUT")
    public Double getS2() {
        return s2;
    }

    public void setS2_1(Double s2_1) {
        this.s2_1 = s2_1;
    }

    public Double getS2_1() {
        return s2_1;
    }

    public void setS3(double s3) {
        this.s3 = s3;
    }

    public double getS3() {
        return s3;
    }

    public void setS4(double s4) {
        this.s4 = s4;
    }

    public double getS4() {
        return s4;
    }

    public void setNalog(double s5) {
        this.nalog = s5;
    }

    public double getNalog() {
        return nalog;
    }
}
