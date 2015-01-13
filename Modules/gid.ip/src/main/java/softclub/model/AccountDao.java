package softclub.model;

/**
 * Created by gid_000 on 12.01.2015.
 */

import javax.ejb.Stateless;

import by.softclub.fos.model.dao.base.AbstractDao;
import softclub.model.entities.Account;

@Stateless
public class AccountDao extends AbstractDao<Account, Long> {

    public AccountDao() {
        super(Account.class);
    }
}