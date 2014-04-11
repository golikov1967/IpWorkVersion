package softclub.model.entities;

import softclub.model.SessionEJBBean;

import java.util.Date;

//@Table(name = "ACT_WRITER")
public class ActWriter extends Thread {

    Payment payment;

    SessionEJBBean sessionBean;

    public ActWriter(String name, Payment payment, SessionEJBBean sessionBean) {
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
