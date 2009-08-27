package org.vosao.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	private String name;

	@Persistent
	private String parent;
	
	@Persistent(mappedBy = "folder", defaultFetchGroup = "true")
	private List<FileEntity> files;

	public FolderEntity() {
		files = new ArrayList<FileEntity>();
	}
	
	public FolderEntity(String aName) {
		this();
		name = aName;
	}
	
	public FolderEntity(String aName, String aParent) {
		this(aName);
		parent = aParent;
	}

	public void copy(final FolderEntity entity) {
		setName(entity.getName());
		setParent(entity.getParent());
		setFiles(entity.getFiles());
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

	public List<FileEntity> getFiles() {
		return files;
	}

	public void setFiles(List<FileEntity> files) {
		this.files = files;
	}

	public void addFile(final FileEntity file) {
		file.setFolder(this);
		files.add(file);
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

}
