package com.kmware.insystem.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cardholders")
public class CardHolder extends BasicModel implements Serializable {
	private static final long serialVersionUID = -5766896577386917529L;

	protected String cardNumber;
	protected String firstName;
	protected String middleName;
	protected String lastName;
	protected String employeeNumber;
	protected String imagePath;
    protected byte[] image;
    protected String imageType;

    @Column(name = "card_number", nullable = false, length = 16)
	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	@Column(name = "first_name", nullable = false)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "middle_name")
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Column(name = "last_name", nullable = false)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "employee_number", nullable = false)
	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

    @Column(name="image_path")
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Column(name="image")
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Column(name="image_type")
    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public boolean havePhoto() {
        if(this.image != null) return true;
        return this.imagePath == null ? false: true;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cardNumber == null) ? 0 : cardNumber.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CardHolder other = (CardHolder) obj;
        if (cardNumber == null) {
            if (other.cardNumber != null)
                return false;
        } else if (!cardNumber.equals(other.cardNumber))
            return false;
        return true;
    }

	@Override
	public String toString() {
		return "CardHolder [id=" + id + ", cardNumber=" + cardNumber + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName + "]";
	}

	@Override
	public void loadLazyCollections() {
		// TODO Auto-generated method stub
		
	} 
 
}