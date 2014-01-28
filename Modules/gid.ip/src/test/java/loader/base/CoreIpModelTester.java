package loader.base;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;
import de.akquinet.jbosscc.needle.junit.NeedleRule;
import org.apache.log4j.Logger;
import org.junit.Rule;
import softclub.model.SessionEJB;
import softclub.model.SessionEJBBean;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: gid
 * Date: 22.09.13
 * Time: 13:33
 * To change this template use File | Settings | File Templates.
 */
public class CoreIpModelTester {
    @Rule
    public DatabaseRule newDatabaseRule = new DatabaseRule("NewModelIP");
    @Rule
    public NeedleRule needleRule = new NeedleRule(newDatabaseRule);
    @Rule
    public DatabaseRule oldDatabaseRule = new DatabaseRule("OldModelIP");
    @Rule
    public NeedleRule oldNeedleRule = new NeedleRule(oldDatabaseRule);
    @ObjectUnderTest(implementation = SessionEJBBean.class)
    protected SessionEJB newModel;
    @Inject
    protected javax.persistence.EntityManager newEm;
    //    Logger LOGGER = Logger.getRootLogger();
    protected Logger LOGGER = Logger.getLogger("loader.IpModelTester");
}
