package loader.entity;

/**
 * Created by gid_000 on 26.01.14.
 */

import by.softclub.fos.model.dao.base.BaseEntity;
import softclub.model.entities.pk.DeclarationId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity(name = "EasyDeclaration")
@Table(name = "T_EASY_DECL_NEW")
public class EasyDeclaration implements BaseEntity<DeclarationId> {
    double s1;
    double s2;
    double s2_1;
    double s3;
    double s3_1;
    double s4;
    double s5;

    public double getS1() {
        return s1;
    }

    public void setS1(double s1) {
        this.s1 = s1;
    }

    public double getS2() {
        return s2;
    }

    public void setS2(double s2) {
        this.s2 = s2;
    }

    public double getS2_1() {
        return s2_1;
    }

    public void setS2_1(double s2_1) {
        this.s2_1 = s2_1;
    }

    public double getS3() {
        return s3;
    }

    public void setS3(double s3) {
        this.s3 = s3;
    }

    public double getS3_1() {
        return s3_1;
    }

    public void setS3_1(double s3_1) {
        this.s3_1 = s3_1;
    }

    public double getS4() {
        return s4;
    }

    public void setS4(double s4) {
        this.s4 = s4;
    }

    public double getS5() {
        return s5;
    }

    public void setS5(double s5) {
        this.s5 = s5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EasyDeclaration that = (EasyDeclaration) o;

        if (!id.equals(that.getId())) return false;
        if (Double.compare(that.s1, s1) != 0) return false;
        if (Double.compare(that.s2, s2) != 0) return false;
        if (Double.compare(that.s2_1, s2_1) != 0) return false;
        if (Double.compare(that.s3, s3) != 0) return false;
        if (Double.compare(that.s3_1, s3_1) != 0) return false;
        if (Double.compare(that.s4, s4) != 0) return false;
        if (Double.compare(that.s5, s5) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getId().getMonth();
        result = 31 * result + getId().getYear();
        temp = Double.doubleToLongBits(s1);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(s2);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(s2_1);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(s3);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(s3_1);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(s4);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(s5);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    protected DeclarationId id;

    @Override
    @EmbeddedId
    public DeclarationId getId() {
        if(id==null){
            id = new DeclarationId();
        }
        return id;
    }

    @Override
    public void setId(DeclarationId declarationId) {
        id  = declarationId;
    }
}
