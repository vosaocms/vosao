package org.vosao.entity;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FileEntity implements Serializable {

	private static final long serialVersionUID = 3L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key id;
	
	@Persistent
	private String title;
	
	@Persistent(serialized = "true", defaultFetchGroup = "true")
	private DownloadableFile file;

	@Persistent
	private FolderEntity folder;	
	
	public FileEntity() {
	}
	
	public FileEntity(String aTitle, DownloadableFile aFile, 
			FolderEntity aFolder) {
		this();
		title = aTitle;
		file = aFile;
		folder = aFolder;
	}
	
	public FileEntity(String aTitle, String aName, String aContentType,
			byte[] aData, FolderEntity folder) {
		this(aTitle, new DownloadableFile(aName, aContentType, aData), folder);
	}
	
	public void copy(final FileEntity entity) {
		setTitle(entity.getTitle());
		setFile(entity.getFile());
	}
	
	public Key getId() {
		return id;
	}
	
	public void setId(Key id) {
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

	public FolderEntity getFolder() {
		return folder;
	}

	public void setFolder(FolderEntity folder) {
		this.folder = folder;
	}

}
