package org.vosao.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FolderEntity implements Serializable {

	private static final long serialVersionUID = 3L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	
	@Persistent
	private String name;
	
	@Persistent(mappedBy = "folder", defaultFetchGroup = "true")
	private List<FileEntity> files;

	public FolderEntity() {
		files = new ArrayList<FileEntity>();
	}
	
	public FolderEntity(String aName) {
		this();
		name = aName;
	}
	
	public void copy(final FolderEntity entity) {
		setName(entity.getName());
		setFiles(entity.getFiles());
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
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

}
