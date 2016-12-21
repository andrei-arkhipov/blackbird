package com.kmware.insystem.beans.view;

import java.io.*;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.kmware.insystem.beans.helper.BasicLazyDataModel;
import com.kmware.insystem.dao.BasicDAO;
import com.kmware.insystem.dao.CardHolderDAO;
import com.kmware.insystem.model.CardHolder;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ViewScoped
@ManagedBean
public class CardHolderBean extends AbstractViewBean<CardHolder> {
    private static final long serialVersionUID = -5709727336122186684L;

    @Inject
    private CardHolderDAO dao;

    protected String cardNumber;
    protected String firstName;
    protected String middleName;
    protected String lastName;
    protected String employeeNumber;
    private UploadedFile file;

    @Override
    @PostConstruct
    public void init() {
        entity = new CardHolder();
    }

    @Override
    public BasicDAO<CardHolder> getDAO() {
        return dao;
    }

    @Override
    public Object getEntityId() {
        return entity.getId();
    }

    @Override
    public BasicLazyDataModel<CardHolder> getLazyModel() {
        if (lazyModel == null) {
            createLazyModel(new String[] { "cardNumber", "lastName", "firstName", "middleName", "employeeNumber" });
        }
        return lazyModel;
    }

    @Override
    public String update(){
//        if (!validateEditCardNumber()) {
//            return "";
//        }

//        try {
//            this.entity.setCardNumber(new String(this.entity.getCardNumber().getBytes("iso-8859-1"), "UTF-8"));
//            this.entity.setLastName(new String(this.entity.getLastName().getBytes("iso-8859-1"), "UTF-8"));
//            this.entity.setMiddleName(new String(this.entity.getMiddleName().getBytes("iso-8859-1"), "UTF-8"));
//            this.entity.setFirstName(new String(this.entity.getFirstName().getBytes("iso-8859-1"), "UTF-8"));
//            this.entity.setEmployeeNumber(new String(this.entity.getEmployeeNumber().getBytes("iso-8859-1"), "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            System.out.println(e.getMessage());
//        }

        String message = validateFields();
        if(message == null || !"".equals(message)){
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "");
            FacesContext.getCurrentInstance().addMessage("errors", fm);
            FacesContext.getCurrentInstance().validationFailed();
            return "";
        }

        if(this.file != null){
            if(file.getSize() > 1048576)
            	return handleFileSizeExceeded();

            if(!file.getContentType().contains("image/jpeg"))
            	return handleWrongFileContentType();

            byte[] image = new byte[this.file.getContents().length];
            try {
                InputStream is = this.file.getInputstream();
                is.read(image);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(image.length>1048576){
                return handleFileSizeExceeded();
            }

            this.entity.setImageType(this.file.getContentType());
            this.entity.setImage(image);
        }
        return super.update();
    }

    @Override
    public String save(){
    	if (!validateCardNumber()) {
            return "";
        }

//        try {
//            this.entity.setCardNumber(new String(this.entity.getCardNumber().getBytes("iso-8859-1"), "UTF-8"));
//            this.entity.setLastName(new String(this.entity.getLastName().getBytes("iso-8859-1"), "UTF-8"));
//            this.entity.setMiddleName(new String(this.entity.getMiddleName().getBytes("iso-8859-1"), "UTF-8"));
//            this.entity.setFirstName(new String(this.entity.getFirstName().getBytes("iso-8859-1"), "UTF-8"));
//            this.entity.setEmployeeNumber(new String(this.entity.getEmployeeNumber().getBytes("iso-8859-1"), "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        String message = validateFields();
        if(message == null || !"".equals(message)){
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "");
            FacesContext.getCurrentInstance().addMessage("errors", fm);
            FacesContext.getCurrentInstance().validationFailed();
            return "";
        }

        if(this.file != null){
            if(file.getSize() > 1048576)
                return handleFileSizeExceeded();

            if(!file.getContentType().contains("image/jpeg"))
                return handleWrongFileContentType();

            byte[] image = new byte[this.file.getContents().length];
            try {
                InputStream is = this.file.getInputstream();
                is.read(image);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(image.length>1048576){
                return handleFileSizeExceeded();
            }

            this.entity.setImageType(this.file.getContentType());
            this.entity.setImage(image);
        }
        System.out.println("fname = " + this.firstName);
        System.out.println("lname = " + this.lastName);
        System.out.println("mname = " + this.middleName);
        System.out.println("file = " + this.file == null ? true: false);
        return super.save();
    }

    public String handleFileSizeExceeded(){
        ResourceBundle bundle = ResourceBundle.getBundle("messages");
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("max.size.exceed.message"), "");
        FacesContext.getCurrentInstance().addMessage("errors", fm);
        FacesContext.getCurrentInstance().validationFailed();
        return "";
    }

    public String handleWrongFileContentType(){
        ResourceBundle bundle = ResourceBundle.getBundle("messages");
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("image.wrong.content.type.message"), "");
        FacesContext.getCurrentInstance().addMessage("errors", fm);
        FacesContext.getCurrentInstance().validationFailed();
        return "";
    }

    public String validateFields(){
        ResourceBundle bundle = ResourceBundle.getBundle("cardholder_messages");

        if(this.entity.getCardNumber() == null) return bundle.getString("validation.card_number.empty");
        if(this.entity.getCardNumber().equals("")) return bundle.getString("validation.card_number.empty");
        if(this.entity.getCardNumber().length()>16) return bundle.getString("validation.card_number.lenght");

        if(this.entity.getLastName() == null) return bundle.getString("validation.last_name.empty");
        if(this.entity.getLastName().equals("")) return bundle.getString("validation.last_name.empty");
        if(this.entity.getLastName().length()>255) return bundle.getString("validation.last_name.lenght");


        if(this.entity.getFirstName() == null) return bundle.getString("validation.first_name.empty");
        if(this.entity.getFirstName().equals("")) return bundle.getString("validation.first_name.empty");
        if(this.entity.getFirstName().length()>255) return bundle.getString("validation.first_name.lenght");

        if(this.entity.getMiddleName().length()>255) return bundle.getString("validation.middle_name.lenght");

        if(this.entity.getEmployeeNumber().length()>255) return bundle.getString("validation.tabel_number.lenght");

        return "";
    }

    /**
     * Validate users don't exists match
     * @return true if users not exists, false otherwise and provide the faces message
     */
    private boolean validateCardNumber() {
        if (cardNumber != null && cardNumber != "") {
        	if (dao.isCardNumberExist(cardNumber)) {
        		System.out.println("exist = " + dao.isCardNumberExist(cardNumber));
                FacesContext context = FacesContext.getCurrentInstance();
                ResourceBundle bundle = ResourceBundle.getBundle("cardholder_messages");
                String errormsg = bundle.getString("validation.card_number.exist");
                context.addMessage("errors", new FacesMessage(FacesMessage.SEVERITY_ERROR, errormsg, ""));
                context.validationFailed();
                return false;
            }
        }
        entity.setCardNumber(cardNumber);
        return true;
    }

    private boolean validateEditCardNumber() {
        if (cardNumber != null && cardNumber != "" && !cardNumber.equals(entity.getCardNumber())) {
        	if (dao.isCardNumberExist(cardNumber)) {
        		System.out.println("exist = " + dao.isCardNumberExist(cardNumber));
                FacesContext context = FacesContext.getCurrentInstance();
                ResourceBundle bundle = ResourceBundle.getBundle("cardholder_messages");
                String errormsg = bundle.getString("validation.card_number.exist");
                context.addMessage("errors", new FacesMessage(FacesMessage.SEVERITY_ERROR, errormsg, ""));
                context.validationFailed();
                return false;
            }
        }
        entity.setCardNumber(cardNumber);
        return true;
    }

    public StreamedContent getImage(){
        return new DefaultStreamedContent(new ByteArrayInputStream(this.entity.getImage()), "image/jpeg");
    }



    public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        byte[] image = new byte[file.getContents().length];
        try {
            InputStream is = file.getInputstream();
            is.read(image);
            is.close();

            this.entity.setImageType(file.getContentType());
            this.entity.setImage(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        FacesMessage msg = new FacesMessage(event.getFile().getFileName() + " загружена.");        
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}
