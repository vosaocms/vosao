package org.vosao.entity;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public interface BaseEntity extends Serializable {

	Key getKey();

	void setKey(Key key);
	
	Long getId();

	void setId(Long id);
	
	void save(Entity entity);
	
	void load(Entity entity);
	
	boolean isNew();
	
	/**
	 * Copy all fields from entity.
	 * @param entity - data to copy from.
	 */
	void copy(BaseEntity entity);
	
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
