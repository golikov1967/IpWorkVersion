package softclub.model.entities;

import softclub.model.SessionEJB;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "ACT_WRITER")
public class ActWriter extends Thread {

    Payment payment;

    SessionEJB sessionBean;

    public ActWriter(String name, Payment payment, SessionEJB sessionBean) {
        super(name);
        this.sessionBean = sessionBean;
        this.payment     = payment;
    }

    public void run() {
        System.out.println("START " + getName() + "-" + new Date());
        sessionBean.mergePayment(payment);
        System.out.println("END " + getName() + "-" + new Date());
    }
}
