package softclub.model.entities;

import javax.persistence.*;
import javax.xml.datatype.DatatypeConfigurationException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@NamedQueries({ @NamedQuery(
    name  = "Act.findAll",
    query = "select o from Act o"
) })
@Inheritance
@Table(name = "ACT")
public class Act extends Document implements Serializable {

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    private static Map contractNumGetter = new HashMap<String, Integer>();

    private static Map contractDateGetter = new HashMap<String, Integer>();

    private static Map actNumGetter = new HashMap<String, Integer>();

    private static Map actDateGetter = new HashMap<String, Integer>();

    static{
        contractNumGetter.put("(СОГЛ. ДОГ. №)(.*)( ОТ ).*АКТ", 2);
        contractNumGetter.put("(ПО ДОГОВОРУ N)(.*)( ОТ )", 2);
        contractNumGetter.put("(по договору )(.*)( ОТ )", 2);
        contractNumGetter.put("( ДОГОВОР )(.*)( ОТ )", 2);
        contractNumGetter.put("(ПО СОГЛАСНО ДОГОВОРА )(.*)(ОТ)", 2);

        actNumGetter.put("( И АКТУ N)(.*)(ОТ)", 2);
        actNumGetter.put("(Г.)(.*)(БЕЗ НДС.)", 2);
        actNumGetter.put("(ПРОГРАММНОГО ОБЕСПЕЧЕНИЯ АКТ )(.*)( ОТ )", 2);

        actDateGetter.put("( ОТ )(.*)(Г. БЕЗ НДС.)", 2);
        actDateGetter.put("(АКТ ВЫП. РАБ. ОТ)(.*)(Г.)", 2);
        actDateGetter.put("( ОТ )(.*)( ДОГОВОР )", 2);

        contractDateGetter.put("( ОТ )(.*)(Г. И АКТУ )", 2);
        contractDateGetter.put("( ОТ )(.*)(г. Без НДС)", 2);
        contractDateGetter.put("( ОТ )(.*)( БЕЗ НДС.)", 2);
    }

    private Contract contract;

    private Set<Payment> paymentList = new HashSet<Payment>(0);

    @OneToMany(mappedBy = "act")
    public Set<Payment> getPaymentList() {
        return paymentList;
    }

    @ManyToOne
    public Contract getContract() {
        return contract;
    }

    public void setPaymentList(Set<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public Act() {
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    private static String getString4Pattern(String string, Map re) {
        String result = null;

        for (Object pattern : re.keySet().toArray()) {
            result = extractLikedString(string, (String) pattern, (Integer) re.get(pattern));
        }

        return result;
    }

    private static String extractLikedString(String AInputText, String patternString, int index) {
        String  input   = AInputText;
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            if (matcher.groupCount() > index) {
                return matcher.group(index).trim();
            }
        }

        return null;
    }

    private static Date parseDate(String stringDate)
            throws SQLException, DatatypeConfigurationException, ParseException {
        String dateFormat = "";

        if (stringDate == null) {
            return null;
        } else if (stringDate.indexOf("/") > -1) {
            if (stringDate.length() < 10) {
                dateFormat = "dd/MM/yy";
            } else {
                dateFormat = "dd/MM/yyyy";
            }
        } else if (stringDate.indexOf(".") > -1) {
            if (stringDate.length() < 10) {
                dateFormat = "dd.MM.yy";
            } else {
                dateFormat = "dd.MM.yyyy";
            }
        } else if (stringDate.indexOf(",") > -1) {
            if (stringDate.length() < 10) {
                dateFormat = "dd,MM,yy";
            } else {
                dateFormat = "dd,MM,yyyy";
            }
        } else {
            LOGGER.severe("Неверный формат даты:" + stringDate);

            return null;
        }

        DateFormat myDateFormat = new SimpleDateFormat(dateFormat);
        final Date myDate       = myDateFormat.parse(stringDate);

        return myDate;
    }

    public static Act parseAct(Payment p) {
        String payNote = p.getPayNote();
        Act    act     = p.getAct();

        if (act == null) {
            try {
                act = new Act();

                act.setDocNumber(getString4Pattern(payNote, actNumGetter));

                try {
                    act.setDocDate(parseDate(getString4Pattern(payNote, actDateGetter)));

                    Contract c = act.getContract();

                    if (c == null) {
                        c = new Contract();
                    }

                    c.setDocNumber(getString4Pattern(payNote, contractNumGetter));
                    c.setDocDate(parseDate(getString4Pattern(payNote, contractDateGetter)));
                    act.setContract(c);
                } catch (SQLException e) {
                    LOGGER.throwing("SQLException", "loadBanksFromDB()", e);
                } catch (DatatypeConfigurationException e) {
                    LOGGER.throwing("DatatypeConfigurationException", "loadBanksFromDB()", e);
                } catch (ParseException e) {
                    LOGGER.throwing("ParseException", "loadBanksFromDB()", e);
                }

                if (act == null) {
                    LOGGER.finest(payNote);
                } else {
                    p.setAct(act);

//                  ActWriter aw = new ActWriter(p.getDocDate().toString(), p, this);
//
//                  aw.start();
                }
            } catch (Exception e) {
                LOGGER.throwing("SQLException", payNote, e);
            }
        }

        return act;
    }
}
