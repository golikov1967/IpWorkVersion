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

    public static Date getLastDayOfMounth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH,1);
        cal.set(Calendar.DATE,1);
        cal.add(Calendar.DATE,-1);
        return cal.getTime();
    }
}
