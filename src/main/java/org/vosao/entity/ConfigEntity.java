package org.vosao.entity;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ConfigEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	
	@Persistent
	private String name;
	
	@Persistent
	private String value;

	public ConfigEntity() {
	}
	
	public ConfigEntity(String aName, String aValue) {
		this();
		name = aName;
		value = aValue;
	}
	
	public void copy(final ConfigEntity entity) {
		setName(entity.getName());
		setValue(entity.getValue());
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String aValue) {
		value = aValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
