package by.softclub.fos.report.impl;

import oracle.apps.xdo.XDOException;
import oracle.apps.xdo.template.FOProcessor;
import oracle.apps.xdo.template.RTFProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Locale;

public class ReportBuilder {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ReportBuilder.class);
    private static final String ENCODING = "UTF-8";
    private static final String EMPTY_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>";

    private final InputStream xdo;
    private final InputStream templateStream;

    public ReportBuilder(final InputStream xdo, InputStream templateStream) {
        this.xdo = xdo;
        this.templateStream = templateStream;
    }

    public byte[] buildReport(final ReportProperties properties, InputStream inputStream) {
        final boolean isBlank = properties.isBlank();

        try {
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, ENCODING);
            final Reader xmlReader = !isBlank ? inputStreamReader : new StringReader(EMPTY_XML);
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            final ByteArrayOutputStream reportStream = new ByteArrayOutputStream();
            // Generating XSL with Input/Output Stream
            final RTFProcessor rtfProcessor = new RTFProcessor(templateStream);
            rtfProcessor.setOutput(reportStream);
            rtfProcessor.process();
            //Generating Output from an XML File and an XSL File
            final FOProcessor processor = new FOProcessor();
            processor.setLocale(new Locale(properties.getReportLocale()));
            processor.setData(xmlReader);
            processor.setTemplate(new ByteArrayInputStream(reportStream.toByteArray()));
            processor.setConfig(xdo);
            CReportType reportType = properties.getReportType();
            processor.setOutputFormat(reportType != null ? reportType.getBiPublisherFormat() : CReportType.HTML.getBiPublisherFormat());
            processor.setOutput(outputStream);
            processor.generate();
            return outputStream.toByteArray();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (XDOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}
