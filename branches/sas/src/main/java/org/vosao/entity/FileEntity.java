package org.vosao.entity;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FileEntity implements Serializable {

	private static final long serialVersionUID = 3L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	
	@Persistent
	private String title;
	
	@Persistent(serialized = "true", defaultFetchGroup = "true")
	private DownloadableFile file;

	public FileEntity() {
	}
	
	public FileEntity(String aTitle, DownloadableFile aFile) {
		this();
		title = aTitle;
		file = aFile;
	}
	
	public FileEntity(String aTitle, String aName, String aContentType,
			byte[] aData) {
		this(aTitle, new DownloadableFile(aName, aContentType, aData));
	}
	
	public void copy(final FileEntity entity) {
		setTitle(entity.getTitle());
		setFile(entity.getFile());
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public DownloadableFile getFile() {
		return file;
	}

	public void setFile(DownloadableFile file) {
		this.file = file;
	}

}
