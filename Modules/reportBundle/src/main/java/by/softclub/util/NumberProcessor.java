package by.softclub.util;

import by.softclub.util.text.WritableSumm;
import by.softclub.util.text.WritableSummRuBYR;

/**
 * Created with IntelliJ IDEA.
 * User: gid
 * Date: 08.04.14
 * Time: 8:00
 */
public class NumberProcessor {
    private static final String classPrefix = "by.softclub.util.text.WritableSumm";
    private String lang;
    private String currency;

    public NumberProcessor(String lang, String currency) {
        this.lang = lang.substring(0,1).toUpperCase() + lang.substring(1);
        this.currency = currency;
    }

    public String numberToString(Number num){
        WritableSumm processor;
        try {
            final Class processorClass = Class.forName(classPrefix + lang + currency);
            processor = (WritableSumm) processorClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            processor = new WritableSummRuBYR();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            processor = new WritableSummRuBYR();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            processor = new WritableSummRuBYR();
        }
        return processor.numberToString(num);
    }
}
