package by.softclub.util.text;

/**
 * Created by gid on 07.04.14.
 */
abstract class AbstractWritableSumm implements WritableSumm {
    String[][] str1;
    String[] str100;
    String[] str11;
    String[] str10;
    protected String getS1(int n, int gender) {
        return str1[gender][n];
    }
    protected String getS11(int n) {
        return str11[n];
    }
    protected String getS10(int n) {
        return str10[n];
    }
    protected String getS100(int n) {
        return str100[n];
    }
    // преобразование триады в слова
    protected StringBuffer triadToString(int n, int gender, boolean acceptZero) {
        StringBuffer res = new StringBuffer();
        if (!acceptZero && n == 0) return res;
        if (n % 1000 > 99) {
            res.append(getS100(n % 1000 / 100)).append(" ");
        }
        if (n % 100 > 10 && n % 100 < 20) {
            return res.append(getS11(n % 10) + " ");
        }
        if (n % 100 > 9) {
            res.append(getS10(n % 100 / 10)).append(" ");
        }
        if (res.length() == 0 || n % 10 > 0) {
            res.append(getS1(n % 10, gender)).append(" ");
        }
        return res;
    }
    // получение юнита (название триады или валюта)
    abstract protected String getUnit(int idx, long count);
    // форма юнита (для русского языка - пол)
    abstract protected int getUnitGender(int idx);
    // главный метод
    public String numberToString(Number num) {
        StringBuffer res = new StringBuffer();
        if (num.longValue() == 0) {
            res.append(getS1(0, 0)).append(" ").append(getUnit(1, 0));
        }
        int idx = 0;
        num = num.longValue() * 1000 + (long)((num.doubleValue() - num.longValue()) * 100);
        while (num.longValue() > 0) {
            StringBuffer triad = triadToString((int)(num.longValue() % 1000),
                    getUnitGender(idx), idx < 1);
            res.insert(0, triad.append(getUnit(idx, num.longValue() % 1000)).append(" "));
            num = num.longValue() / 1000;
            idx++;
        }
        return res.substring(0,1).toUpperCase() + res.substring(1).toString().trim();
    }
}