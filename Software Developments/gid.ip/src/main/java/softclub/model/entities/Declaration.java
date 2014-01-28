package softclub.model.entities;

import by.softclub.fos.model.dao.base.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "DECLARATION")
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
            this.setTotalInputFromBeginYear(prev.getTotalInputFromBeginYear() +
                                            prev.getTotalInput());
        }
    }

    public SuperDeclaration getPrev() {
        return prev;
    }

    public void setQuartal(SuperDeclaration quartal) {
        if (this.quartal != quartal) {
            this.quartal = quartal;
            this.quartal.setTotalInput(this.quartal.getTotalInput() +
                                       this.getTotalInput());
        }
    }

    public SuperDeclaration getQuartal() {
        return quartal;
    }


}
