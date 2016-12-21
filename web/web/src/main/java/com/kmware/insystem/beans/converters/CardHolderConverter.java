package com.kmware.insystem.beans.converters;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang3.StringUtils;

import com.kmware.insystem.model.CardHolder;

public class CardHolderConverter implements Converter, Serializable  {
    private static final long serialVersionUID = 8102427149169648951L;

    private List<CardHolder> cardHolders = Collections.emptyList();

    public List<CardHolder> getCardHolders() {
        return cardHolders;
    }

    public void setCardHolders(List<CardHolder> cardHolders) {
        this.cardHolders = cardHolders;
    }

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        if (StringUtils.isNotBlank(arg2)) {
            System.out.println("CardHolder CONVERTER to object: "+arg2);
            for (CardHolder t : cardHolders) {
                if (t.getId() == Long.valueOf(arg2)) {
                    return t;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        if (arg2 instanceof CardHolder) {
            System.out.println("CardHolder CONVERTER to string: "+Long.toString(((CardHolder) arg2).getId()));
            return Long.toString(((CardHolder) arg2).getId());
        }
        return "";
    }

}
