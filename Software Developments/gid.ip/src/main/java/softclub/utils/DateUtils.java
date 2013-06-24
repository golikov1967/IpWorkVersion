package softclub.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public DateUtils() {
        super();
    }

    public static boolean isLastQuartalMounth(Date date) {
        boolean result = true;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            result = (cal.get(Calendar.MONTH) % 3 == 0);
        }
        return result;
    }

    public static Date getFirstDayOfMounth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE,1);
        return cal.getTime();
    }
}
