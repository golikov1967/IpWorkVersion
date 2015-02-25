package softclub.model;

import by.softclub.fos.model.dao.base.AbstractDao;
import softclub.model.entities.Act;
import softclub.model.entities.PayType;
import softclub.model.entities.pk.DocumentId;

import javax.ejb.Stateless;

@Stateless
public class PayTypeDao extends AbstractDao<PayType, String> {

    public PayTypeDao() {
        super(PayType.class);
    }
}
