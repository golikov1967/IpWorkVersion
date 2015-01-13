package by.softclub.fos.report.impl;

/**
 * User: chgv
 * Date: 12.12.13
 * Time: 11:12
 */
public class GeneratedReport {
    private String reportName;
    private CReportType reportType;
    private byte[] reportBody;

    public GeneratedReport(final String reportName, final CReportType reportType, final byte[] reportBody) {
        this.reportName = reportName;
        this.reportType = reportType;
        this.reportBody = reportBody;
    }

    public String getReportName() {
        return reportName;
    }

    public CReportType getReportType() {
        return reportType;
    }

    public byte[] getReportBody() {
        return reportBody;
    }

    public String getFullReportName() {
        return reportName + "." + reportType.getExtension();
    }
}
