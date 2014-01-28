package softclub.model;

import by.softclub.fos.model.dao.base.AbstractDao;
import loader.entity.EasyDeclaration;
import softclub.model.entities.Declaration;

import javax.ejb.Stateless;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

@Stateless(name = "SessionEJB", mappedName = "TestWS-ModelJP-SessionEJB")
public class DeclarationDao extends AbstractDao<Declaration, Long> {

    private static final String NOT_PARSED = "Не разобрано:";

    private static final Logger LOGGER = Logger.getAnonymousLogger();


    public DeclarationDao() {
        super(Declaration.class);
    }

    public Declaration calcDeclaration(int iYear, int iMonth) {
        final int begMonth;
        if(iYear == 2007){
            begMonth = 6;
        }else{
            begMonth = 0;
        }

        EasyDeclaration atrs = getNewDeclAttrs(iMonth, begMonth, iYear);

        return null;
    }

    private EasyDeclaration getNewDeclAttrs(int iMonth, int begMonth, int currYear) {
        Calendar date = new GregorianCalendar();
        date.setTime(new Date());
        date.set(Calendar.DAY_OF_MONTH, 1);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        if(currYear==0){
            currYear = date.get(Calendar.YEAR);
        }
        date.set(Calendar.MONTH, iMonth);
        Date calcDate = date.getTime();

        final int procent;
        if (calcDate.after(getDate('01.01.2013', 'DD.MM.YYYY'))){
            procent = 5;
        } else if(calcDate.after(getDate('01.01.2012', 'DD.MM.YYYY'))){
            procent = 7;
        } else if(calcDate.after(getDate('01.01.2009', 'DD.MM.YYYY'))){
            procent = 8;
        }else{
            procent = 10;
        }

    }
}
