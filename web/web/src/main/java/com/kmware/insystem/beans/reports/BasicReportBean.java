package com.kmware.insystem.beans.reports;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;

public abstract class BasicReportBean {

    protected enum ReportType {
        PDF, XLS, EXCEL, HTML;
    }

    private static final String DS_JNDI_NAME = "java:jboss/datasources/inBoxDS";
    private static final String TEMP_DIR = "/tmp";

    public abstract String getTemplateFileName();

    public abstract String getReportFileName();

    protected abstract Map<String, Object> processParams();

    public void asPDF() {
        try {
            fillReport(ReportType.PDF);
        } catch (JRException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void asXLS() {
        try {
            fillReport(ReportType.XLS);
        } catch (JRException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void fillReport(ReportType type) throws JRException, NamingException, SQLException, IOException {
        HttpServletResponse resp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
      //  String template = getTemplateFileName();
        JasperPrint jasperPrint = null;
        InputStream is =null;
        Map<String, Object> params = processParams();

        // creating the virtualizer
        File tmp = new File(new File(TEMP_DIR),"jasper"+new Date().getTime());
        if(!tmp.exists()){
            tmp.mkdir();
        }
		
        JRFileVirtualizer virtualizer = new JRFileVirtualizer(5,tmp.getAbsolutePath());
        params.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
        params.put(JRXlsAbstractExporterParameter.PROPERTY_MAXIMUM_ROWS_PER_SHEET , "1000");
        JasperPrint printer = new JasperPrint();

		File file = new File(getTemplateFileName());
        is = new FileInputStream(file);
        jasperPrint = JasperFillManager.fillReport(is, params, getConnection(""));
		
        byte[] reportData = new byte[0];
        String contentType = "";
        String reportFileName = "";

        switch (type) {
            case PDF:
                reportData = generatePdfReport(jasperPrint).toByteArray();
                contentType = "application/pdf";
                reportFileName = getReportFileName() + ".pdf";
                break;
            case XLS:
                reportData = generateXlsReport(jasperPrint).toByteArray();
                contentType = "application/xls";
                reportFileName = getReportFileName() + ".xls";
                break;
            case EXCEL:
                break;
            case HTML:
                break;
            default:
                break;
        }

        resp.setContentType(contentType);
        resp.setContentLength(reportData.length);
        resp.setHeader("Content-disposition", "attachment; filename=\"" + reportFileName + "\"");
        resp.getOutputStream().write(reportData);
        resp.getOutputStream().flush();
        virtualizer.cleanup();
        tmp.delete();
        if(is!=null){is.close();}
        FacesContext.getCurrentInstance().responseComplete();
    }

    protected ByteArrayOutputStream generateExcelReport(JasperPrint jasperPrint) throws JRException {
        ByteArrayOutputStream reportStream = new ByteArrayOutputStream();
        JExcelApiExporter xlsExporter = new JExcelApiExporter();
        xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        xlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, reportStream);
        xlsExporter.exportReport();
        return reportStream;
    }

    protected ByteArrayOutputStream generateXlsReport(JasperPrint jasperPrint) throws JRException {
        ByteArrayOutputStream reportStream = new ByteArrayOutputStream();
        JRXlsExporter xlsExporter = new JRXlsExporter();
        xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        xlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, reportStream);
        xlsExporter.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET , Integer.valueOf(1000));
        xlsExporter.exportReport();
        return reportStream;
    }

    protected ByteArrayOutputStream generatePdfReport(JasperPrint jasperPrint) throws JRException {
        ByteArrayOutputStream reportStream = new ByteArrayOutputStream();
        JRPdfExporter pdfExporter = new JRPdfExporter();
        pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        pdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, reportStream);
        pdfExporter.exportReport();
        return reportStream;
    }

    protected Connection getConnection(String jndiName) throws NamingException, SQLException {
        Context ctx = new InitialContext();
        Object o = ctx.lookup((jndiName != null && !jndiName.equals("")) ? jndiName : DS_JNDI_NAME);
        if (o != null) {
            return ((DataSource) o).getConnection();
        }
        return null;
    }

}
