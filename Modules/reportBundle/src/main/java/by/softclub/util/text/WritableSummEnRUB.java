package by.softclub.util.text;

/**
 * Created by gid on 07.04.14.
 */
public class WritableSummEnRUB extends WritableSummEn {
    {
        s4[0] = "kop";
        s4[1] = "rouble";
    }
    @Override
    protected String getUnit(int idx, long count) {
        return s4[idx] + (idx != 0 && count != 1 ? "s" : "");
    }
}