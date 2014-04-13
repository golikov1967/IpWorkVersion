package softclub.model;

import by.softclub.fos.model.dao.base.AbstractDao;
import softclub.model.entities.Declaration;
import softclub.model.entities.Declaration_;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class DeclarationDao extends AbstractDao<Declaration, Long> {

    @EJB
    public InputPaymentDao inputPaymentDao;

    @EJB
    public OutputPaymentDao outputPaymentDao;

    private static final String NOT_PARSED = "Не разобрано:";

    private static final Logger LOGGER = Logger.getAnonymousLogger();


    public DeclarationDao() {
        super(Declaration.class);
    }

    public Declaration calcDeclaration(int iYear, int iMonth, Declaration prev) {
        final int begMonth;
        if (iYear == 2007) {
            begMonth = 6;
        } else {
            begMonth = 0;
        }

        Date date4Params = getDate4Params(iMonth, iYear);
        Declaration result = findDeclaration(date4Params);
        if (result == null) {
            result = getNewDeclAttrs(iMonth, begMonth, iYear, prev);
            merge(result);
        }


        return result;
    }

    public Declaration findDeclaration(Date date4Params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Declaration> criteriaQuery = cb.createQuery(Declaration.class);
        Root<Declaration> root = criteriaQuery.from(type);

        criteriaQuery.where(
                cb.equal(root.get(Declaration_.beginDate), date4Params)
        );
        TypedQuery<Declaration> query = em.createQuery(criteriaQuery);
        List<Declaration> resultList = query.setMaxResults(1).getResultList();
        return resultList != null && resultList.size() > 0 ? resultList.get(0) : null;
    }

    private Declaration getNewDeclAttrs(int iMonth, int begMonth, int currYear, Declaration prev) {
        Date calcDate = getDate4Params(iMonth, currYear);

        final int procent;
        if (calcDate.after(getDate("01.01.2013", "DD.MM.YYYY"))) {
            procent = 5;
        } else if (calcDate.after(getDate("01.01.2012", "DD.MM.YYYY"))) {
            procent = 7;
        } else if (calcDate.after(getDate("01.01.2009", "DD.MM.YYYY"))) {
            procent = 8;
        } else {
            procent = 10;
        }
        Declaration result = new Declaration();
        result.setBeginDate(calcDate);
        // взять сумму приходов за период
        final double inSumm = inputPaymentDao.getTestSum4Date(currYear, 0, iMonth).doubleValue();

        // вычесть сумму возвратов за период
        final double minusSumm = outputPaymentDao.getMinusSum4Date(iMonth, currYear).doubleValue();
        result.setTotalInputYear(inSumm - minusSumm);

        // налоговая база ВСЕГО - (сумма поступлений за период)
        result.setS2(inputPaymentDao.getInputSum4Date(currYear, begMonth, iMonth).doubleValue());

        // налога по текущей ставке
        result.setS2_1(result.getS2());

        // сумма налога по расчету
        result.setS3(result.getS2_1() / 100 * procent);

        result.setS4(prev.getS3());

        result.setNalog(result.getS3() - result.getS4());



        return result;
    }

    public Date getDate4Params(int iMonth, int currYear) {
        Calendar date = new GregorianCalendar();
        date.setTime(new Date());
        date.set(Calendar.DAY_OF_MONTH, 1);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        date.set(Calendar.YEAR, currYear);
        date.set(Calendar.MONTH, iMonth - 1);
        return date.getTime();
    }

    private Date getDate(String source, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
