package org.vosao.entity;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FolderEntity implements Serializable {

	private static final long serialVersionUID = 3L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;
	
	@Persistent
	private String title;

	@Persistent
	private String name;

	@Persistent
	private String parent;
	
	public FolderEntity() {
	}
	
	public FolderEntity(String aName) {
		this();
		name = aName;
		title = aName;
	}
	
	public FolderEntity(String aName, String aParent) {
		this(aName);
		parent = aParent;
	}

	public FolderEntity(String aTitle, String aName, String aParent) {
		this(aName, aParent);
		title = aTitle;
	}

	public void copy(final FolderEntity entity) {
		setName(entity.getName());
		setTitle(entity.getTitle());
		setParent(entity.getParent());
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

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
