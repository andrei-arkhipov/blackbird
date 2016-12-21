package com.kmware.insystem.beans.helper;

import javax.ejb.Local;

@Local
public interface IValueHolder {

	public abstract Object getValueFor(Object o);

	public abstract void setValueFor(Object o, Object v);

}