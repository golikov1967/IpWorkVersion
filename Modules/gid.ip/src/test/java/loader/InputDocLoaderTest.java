package loader;

import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: gid
 * Date: 14.04.14
 * Time: 10:13
 */
public class InputDocLoaderTest extends InputDocTester {
    @Test
    public void loadFromSoftclub() throws ParseException {
        //массив парамеров приходных документов
        Object[][] params = new Object[][]{
                {"NOT_NUMBERED", parseDate("01/01/2999"), 1550400d, "Р15/1", parseDate("03/01/2014"), "14/3-3", parseDate("03/03/2014")},
                {"признак окончания массива"}
        };
        //загрузка массива
        for(int i = 0; i< params.length; i++){
            if(params[i].length==7){
                nDoc = (String) params[i][0];
                payDate = (Date) params[i][1];
                paySumm = (Double) params[i][2];
                nContract = (String) params[i][3];
                contractDate = (Date) params[i][4];
                actNumber = (String) params[i][5];
                actDate = (Date) params[i][6];
                fromSoftclub();
            }
        }
    }
}
