package com.kmware.insystem.dao;

import javax.ejb.Stateless;

import com.kmware.insystem.model.CardHolder;

import java.util.List;

@Stateless
public class CardHolderDAO  extends BasicDAO<CardHolder>{
    private static final long serialVersionUID = -8368536954853334060L;

    @SuppressWarnings("unchecked")
	public List<String> cardNumbers(String query){
        return (List<String>) em.createQuery("select t.cardNumber from CardHolder t where t.cardNumber LIKE '%"+ query +"%'").getResultList();
    }

    @SuppressWarnings("rawtypes")
	public boolean isCardNumberExists(String number){
        List list = em.createQuery("select t from CardHolder t where t.cardNumber = '" + number + "'").getResultList();
        return list.isEmpty();
    }

    public CardHolder getCardHolder(String id){
        return (CardHolder) em.createQuery("select t from CardHolder t where t.id = " + Long.valueOf(id)).getSingleResult();
    }

    public Boolean isCardNumberExist(String cardNumber) {
    	Long result =(Long)em.createQuery("SELECT COUNT(*) FROM CardHolder t WHERE t.cardNumber=:cardNumber")
                .setParameter("cardNumber", cardNumber).getSingleResult();	
    	return (result !=0) ? true:false;
    }
}