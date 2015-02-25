package by.softclub.fos.model.dao.base;

import java.io.Serializable;
import java.rmi.Remote;

/**
 * User: chgv
 * Date: 18.12.13
 * Time: 9:29
 */
public interface BaseEntity<ID> extends Serializable, Remote {

    ID getId();

    void setId(ID id);
}
