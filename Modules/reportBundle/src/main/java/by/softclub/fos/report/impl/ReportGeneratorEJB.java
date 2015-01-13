package by.softclub.fos.report.impl;

//import by.softclub.fos.exception.FosException;
import com.sun.java.util.collections.Hashtable;
import oracle.apps.xdo.XDOException;
import oracle.apps.xdo.dataengine.DataProcessor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReportGeneratorEJB {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ReportGeneratorEJB.class);

    private static final String BLANK_NAME = "Blank";

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Inject
    protected EntityManager em;

    public GeneratedReport generateReport(final ReportProperties properties, Path inputXmlPath, final Path xdoFileName) throws Exception {
        final boolean isBlank = properties.isBlank();
        CReportType reportType = properties.getReportType();
        //final String type = properties.getReportKind();
        //final String name = properties.getReportName();

//        final String reportName;
//        if (type != null && !isBlank) {
//            reportName = name + type;
//        } else {
//            reportName = name;
//        }

        GeneratedReport report = null;
        //get the property value and print it out

        try(InputStream templateStream = getTemplateStream(isBlank, properties.getReportName(), xdoFileName.getParent().toString());
            InputStream inputXmlStream = Files.newInputStream(inputXmlPath);
            InputStream configStream = Files.newInputStream(xdoFileName)) {
            final ReportBuilder reportBuilder = new ReportBuilder(configStream, templateStream);

            final byte[] reportBody = reportBuilder.buildReport(properties, inputXmlStream);
            if(reportBody==null){
                throw new Exception("empty.report");
            }
            report = new GeneratedReport(properties.getReportName()+properties.getReportKind(), reportType, reportBody);
        } catch (final FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new Exception("technical.error");
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new Exception("technical.error");
        }
        if (report == null) {
            LOGGER.error("Выполните настройку сервера согласно документу:\nhttps://scwiki/pages/viewpage.action?pageId=13336620");
            throw new Exception("technical.error");
        }
        return report;
    }

    /**
     * отдает Stream шаблона отчета по имени файла
     * @param isBlank
     * @param reportName
     * @param reportTemplatesPath
     * @return
     * @throws IOException
     */
    private InputStream getTemplateStream(boolean isBlank, String reportName, String reportTemplatesPath) throws IOException {
        InputStream templateStream;
        final String templateFileName = concat(reportTemplatesPath, reportName, (isBlank ? BLANK_NAME : reportName) + "." + CReportType.RTF.getExtension());
        final URL reportUrl = getUrlFromFilename(templateFileName);
        templateStream = reportUrl.openStream();
        return templateStream;
    }

    private URL getUrlFromFilename(String concat) throws MalformedURLException {
        return new File(concat).toURI().toURL();
    }

    public String concat(final String... params) {
        final String filePath = StringUtils.join(params, "/");
        // make it all unix-like
        return filePath.replaceAll("[////]+", "/");
    }

    /**
     *
     * @param sql - нативный SQL вида select f1,f2,fn from anyWhere where f1 in (:p1,:p2:p3) or :otherParam = f2
     * @param params - параметры p1, p2, p3... соответствующие запросу проинициализированные кодом вида:
     *    Hashtable params = new Hashtable();
     *    params.put("p_langcode","ru");
     *    params.put("p_busprocinstid",Long.toString(2350l));
     * @param rowsetTag - название элемента, содержащего набор записей
     * @param rowTag - название элемента, содержащего одну запись
     * @return поток с XML-резалтом
     * @throws SQLException
     */
    public InputStream getXML(String sql, Map<String, Object> params, String rowsetTag, String rowTag) throws SQLException {
        DataProcessor dataProcessor = new DataProcessor();

        dataProcessor.setConnection(connection);

        try {
            // Set the SQL to be executed
            dataProcessor.setSql(sql);


            //Set the params
            if(params!=null){
                Hashtable parameters = new Hashtable();
                for(Map.Entry param: params.entrySet()){
                    parameters.put(param.getKey(), param.getValue());
                }
                dataProcessor.setParameters(parameters);
            }


            //Specify the output file name and location
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            dataProcessor.setOutput(os);

            //Specify the root element tag name for the generated output
            if(rowsetTag==null){
                rowsetTag = "DATA_DS";
            }
            dataProcessor.setRowsetTag(rowsetTag);

            //Specify the row elemen tag name for the generated outputt
            if(rowTag==null){
                rowTag = "G_1";
            }
            dataProcessor.setRowTag(rowTag);

            //Execute the SQL
            dataProcessor.processData();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(os.toByteArray());
            return byteArrayInputStream;
        } catch (XDOException e) {
            throw new EJBException(e);
        }
    }

    public <T> InputStream getXML(T currencyExchangeData) throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(currencyExchangeData.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        jaxbMarshaller.marshal(currencyExchangeData, os);
        os.flush();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(os.toByteArray());
        return byteArrayInputStream;
    }
}

