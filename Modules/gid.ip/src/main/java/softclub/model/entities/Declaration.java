package softclub.model.entities;

import by.softclub.fos.model.dao.base.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "DECLARATION")
@Access(value = AccessType.PROPERTY)
public class Declaration extends SuperDeclaration implements BaseEntity<Long> {


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PREV_ID")
    private SuperDeclaration prev;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUARTAL_ID")
    private SuperDeclaration quartal;

    public Declaration() {
    }


    public void setPrev(SuperDeclaration prev) {
        if (this.prev != prev) {
            this.prev = prev;
            this.setTotalInputYear(prev.getTotalInputYear() +
                                            prev.getS2());
        }
    }

    public SuperDeclaration getPrev() {
        return prev;
    }

    public void setQuartal(SuperDeclaration quartal) {
        if (this.quartal != quartal) {
            this.quartal = quartal;
            this.quartal.setS2(this.quartal.getS2() +
                                       this.getS2());
        }
    }

    public SuperDeclaration getQuartal() {
        return quartal;
    }


}
