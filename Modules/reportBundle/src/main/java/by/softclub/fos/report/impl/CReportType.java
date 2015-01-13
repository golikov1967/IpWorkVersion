package by.softclub.fos.report.impl;


import oracle.apps.xdo.template.FOProcessor;

/**
 * User: chgv
 * Date: 12.12.13
 * Time: 10:36
 */
public enum CReportType {

    RTF("rtf", FOProcessor.FORMAT_RTF, "application/rtf"),
    DOC("doc", FOProcessor.FORMAT_RTF, "application/msword"),
    PDF("pdf", FOProcessor.FORMAT_PDF, "application/pdf"),
    HTM("htm", FOProcessor.FORMAT_HTML, "text/html"),
    HTML("html", FOProcessor.FORMAT_HTML, "text/html"),
    XSL("xsl", FOProcessor.FORMAT_EXCEL, "application/vnd.ms-excel");

    private final String extension;
    private final byte biPublisherFormat;
    private final String mimeType;

    private CReportType(String extension, byte biPublisherFormat, String mimeType) {
        this.extension = extension;
        this.biPublisherFormat = biPublisherFormat;
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public byte getBiPublisherFormat() {
        return biPublisherFormat;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static CReportType parse(final String value) {
        CReportType result = null;
        if (value != null) {
            final String prepared = value.toLowerCase().trim();
            for (final CReportType type : values()) {
                if (type.getExtension().equals(prepared)) {
                    result = type;
                    break;
                }
            }
        }
 /*       if (result == null) {
            throw new IllegalArgumentException("Can't parse report type: " + value);
        }*/
        return result;
    }
}
