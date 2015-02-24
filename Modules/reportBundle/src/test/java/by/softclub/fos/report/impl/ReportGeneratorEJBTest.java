package by.softclub.fos.report.impl;

import by.softclub.util.JaxbUtils;
import by.softclub.util.NumberProcessor;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;
import de.akquinet.jbosscc.needle.junit.NeedleRule;
import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import softclub.model.OutputPaymentDao;
import softclub.model.entities.OutputPayment;
import softclub.model.entities.Payment;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * User: chgv
 * Date: 20.12.13
 * Time: 17:13
 */
public class ReportGeneratorEJBTest {
    private static final boolean IS_BLANK = false;
    private static final String LOCALE = "be";
    private static final String REPORT_KIND = "Standard";
    private static final String REPORT_NAME = "SubjectBaseProfile";
    private static final String INPUT_XML = "report.xml";
    private static final String OUTPUT_PATH = "target/";

    @Rule
    public DatabaseRule newDatabaseRule = new DatabaseRule("NewModelIP");
    @Rule
    public NeedleRule needleRule = new NeedleRule(newDatabaseRule);
    @Rule
    public DatabaseRule oldDatabaseRule = new DatabaseRule("OldModelIP");
    @Rule
    public NeedleRule oldNeedleRule = new NeedleRule(oldDatabaseRule);

    @Inject
    protected javax.persistence.EntityManager newEm;
    //    Logger LOGGER = Logger.getRootLogger();
    protected Logger LOGGER = Logger.getLogger("loader.IpModelTester");

    @Test
    public void connectTest() throws Exception {
        EntityManager oldEm = oldDatabaseRule.getEntityManager();
        //EntityManager newEm = newDatabaseRule.getEntityManager();

        assertNotNull(oldEm);
        assertNotNull(newEm);
    }

    private void saveReportBody(GeneratedReport report) throws IOException {
        if (report.getReportBody() != null) {
            Path target = Paths.get("temp/", report.getFullReportName());
            if(!target.getParent().toFile().exists()){
                Files.createDirectories(target.getParent());
            }
            Files.copy(
                    new ByteArrayInputStream(report.getReportBody()),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }
    }

    @ObjectUnderTest(implementation = ReportGeneratorEJB.class)
    ReportGeneratorEJB reportGenerator;

    @ObjectUnderTest(implementation = OutputPaymentDao.class)
    protected OutputPaymentDao outputPaymentDao;

    @Test
    /**
     * Печать списка платежек, отобранных по их номерам
     */
    public void testGeneratePaymentReport() throws Exception {
        Payment pay = outputPaymentDao.findAny();
        payReportSave(pay);
        List<OutputPayment> payList = outputPaymentDao.findDocs("688", "689");
        for(OutputPayment outPay: payList){
            payReportSave(outPay);
        }
    }

    private void payReportSave(Payment pay) throws Exception {
        assertNotNull(pay);

        NumberProcessor processor = new NumberProcessor("Ru", "RUB");
        pay.setDocSumString(processor.numberToString(pay.getPaySum()));

        String xml = JaxbUtils.getFilePath(pay);
        assertNotNull(xml);

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String reportKind = sdf.format(pay.getId().getDocDate()) + pay.getPayType().getId();

        final GeneratedReport report = reportGenerator.generateReport(
                new ReportProperties().setReportName("payDoc").setReportKind(reportKind),
                Paths.get(xml),
                //Paths.get("D:\\Documents\\gid\\data\\workspace\\SoftIP\\Modules\\reportBundle\\src\\test\\resources\\by\\softclub\\fos\\report\\impl\\report.xml"),
                //Paths.get("src/test/resources/reportTemplates", "payDoc/payDoc.xml"),
                Paths.get("src/test/resources/reportTemplates", "xdo_win.xml"));
        assertNotNull(report);
        saveReportBody(report);
    }

    //TODO Реализовать тест печати декларации
    @Test
    public void testGenerateDeclarationReport() throws Exception {

    }

}
