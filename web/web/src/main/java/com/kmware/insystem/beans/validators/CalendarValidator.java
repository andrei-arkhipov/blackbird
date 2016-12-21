package com.kmware.insystem.beans.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Created with IntelliJ IDEA.
 * Author: kelevra
 */
@FacesValidator("calendarValidator")
public class CalendarValidator implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        java.util.Date date = (java.util.Date) o;
        if(date == null){
            FacesMessage msg =
                    new FacesMessage("Неправильно введена дата",
                            "Введите дату");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }

    }
}
