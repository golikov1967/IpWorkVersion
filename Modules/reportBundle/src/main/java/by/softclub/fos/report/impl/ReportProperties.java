package by.softclub.fos.report.impl;


/**
 * User: chgv
 * Date: 11.12.13
 * Time: 18:12
 */
public  class ReportProperties {
    protected boolean isBlank;          // печать только бланка
    protected CReportType reportType = CReportType.DOC;   // extension of report: pdf, html, xls, doc...
    protected String reportName;
    protected String reportKind;        // kind of report: short, standard, extended
    protected String reportLocale = "ru";

    public boolean isBlank() {
        return isBlank;
    }

    public CReportType getReportType() {
        return reportType;
    }

    public String getReportName() {
        return reportName;
    }

    public String getReportKind() {
        return reportKind;
    }

    public String getReportLocale() {
        return reportLocale;
    }

    public ReportProperties setBlank(boolean blank) {
        isBlank = blank;
        return this;
    }

    public ReportProperties setReportType(CReportType reportType) {
        this.reportType = reportType;
        return this;
    }

    public ReportProperties setReportName(String reportName) {
        this.reportName = reportName;
        return this;
    }

    public ReportProperties setReportKind(String reportKind) {
        this.reportKind = reportKind;
        return this;
    }

    public ReportProperties setReportLocale(String reportLocale) {
        this.reportLocale = reportLocale;
        return this;
    }
}
