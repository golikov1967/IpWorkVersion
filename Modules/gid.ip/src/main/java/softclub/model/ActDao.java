package softclub.model;

import by.softclub.fos.model.dao.base.AbstractDao;
import softclub.model.entities.Act;
import softclub.model.entities.pk.DocumentId;

import javax.ejb.Stateless;

@Stateless
public class ActDao extends AbstractDao<Act, DocumentId> {

    protected ActDao(Class<Act> type) {
        super(type);
    }
}
