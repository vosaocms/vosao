package org.vosao.entity;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FileEntity implements Serializable {

	private static final long serialVersionUID = 4L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;
	
	@Persistent
	private String title;
	
	@Persistent(serialized = "true", defaultFetchGroup = "true")
	private DownloadableFile file;

	@Persistent
	private String filename;	

	@Persistent
	private String folderId;	
	
	public FileEntity() {
	}
	
	public FileEntity(String aTitle, String aName, DownloadableFile aFile, 
			String aFolderId) {
		this();
		title = aTitle;
		filename = aName;
		file = aFile;
		folderId = aFolderId;
	}
	
	public FileEntity(String aTitle, String aName, String aContentType,
			Date mdtime, byte[] aData, String folder) {
		this(aTitle, aName, new DownloadableFile(aContentType, mdtime, aData), 
				folder);
	}
	
	public void copy(final FileEntity entity) {
		setTitle(entity.getTitle());
		setFilename(entity.getFilename());
		setFile(entity.getFile());
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
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

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}

}
