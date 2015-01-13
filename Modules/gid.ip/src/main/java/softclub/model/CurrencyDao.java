package softclub.model;

import by.softclub.fos.model.dao.base.AbstractDao;
import softclub.model.entities.Currency;

/**
 * Created by gid_000 on 13.01.2015.
 */
public class CurrencyDao extends AbstractDao<Currency, String> {

    public CurrencyDao() {
        super(Currency.class);
    }
}