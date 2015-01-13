package loader;

import org.junit.Test;

import java.text.ParseException;
import java.sql.Date;

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
//                {"000401", parseDate("12/02/2014"), 2982000d, "Р15/1", parseDate("03/01/2014"), "14/1-9", parseDate("09/01/2014")},
//                {"000570", parseDate("25/02/2014"), 3726000d, "Р15/1", parseDate("03/01/2014"), "14/1-15", parseDate("15/01/2014")},
//                {"000569", parseDate("25/02/2014"), 3942000d, "Р15/1", parseDate("03/01/2014"), "14/1-21", parseDate("21/01/2014")},
                {"признак окончания массива"}
        };
        loadFromArray(params);

    }

    private void loadFromArray(Object[][] params) throws ParseException {
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
