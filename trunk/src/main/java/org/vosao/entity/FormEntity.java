package org.vosao.entity;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FormEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;
	
	@Persistent
	private String name;
	
	@Persistent
	private String title;

	@Persistent
	private String email;

	@Persistent
	private String letterSubject;

	public FormEntity() {
	}
	
	public FormEntity(String aName, String aEmail) {
		this();
		name = aName;
		email = aEmail;
	}
	
	public void copy(final FormEntity entity) {
		setTitle(entity.getTitle());
		setName(entity.getName());
		setEmail(entity.getEmail());
		setLetterSubject(entity.getLetterSubject());
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLetterSubject() {
		return letterSubject;
	}

	public void setLetterSubject(String letterSubject) {
		this.letterSubject = letterSubject;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
