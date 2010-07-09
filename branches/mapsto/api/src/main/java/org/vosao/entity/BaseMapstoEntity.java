package org.vosao.entity;

import java.util.Date;

import org.mapsto.Entity;

public interface BaseMapstoEntity extends Entity {

	boolean isNew();
	
	// audit fields
	
	String getCreateUserEmail();

	void setCreateUserEmail(String createUserEmail);

	Date getCreateDate();

	void setCreateDate(Date createDate);

	String getModUserEmail();

	void setModUserEmail(String modUserEmail);

	Date getModDate();

	void setModDate(Date modDate);

	String getCreateDateString();
	
	String getModDateString();
}
