package com.kmware.insystem.beans.reports;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
/**
 * Created with IntelliJ IDEA.
 * Author: kelevra
 */
@ViewScoped
@ManagedBean(name="excEventReportBean")
public class ExcEventReportBean extends EventReportBean {
    @Override
    public String getTemplateFileName() {
        return FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/reports/event_log_exception.jasper");
    }
}
