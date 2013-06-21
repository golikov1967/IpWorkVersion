package softclub.model.entities;

import javax.persistence.*;

@Entity
@NamedQueries( { @NamedQuery(name = "Declaration.findAll",
                             query = "select o from Declaration o") })
@Table(name = "DECLARATION")
public class Declaration extends SuperDeclaration {


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
