package com.kmware.insystem.beans.validators;

import com.kmware.insystem.dao.CardHolderDAO;

import javax.faces.bean.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * Author: kelevra
 */
@ManagedBean(name="cardNumberValidator")
@RequestScoped
public class CardNumberValidator implements Validator {
    @Inject
    CardHolderDAO dao;

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {

        if(dao.isCardNumberExists((String) o)){
            FacesMessage msg =
                    new FacesMessage("Неверный номер карточки",
                            "Такого номера не существует");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}
