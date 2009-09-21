package org.vosao.entity;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FileChunkEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;

	@Persistent(serialized = "true", defaultFetchGroup = "true")
	private Blob data;

	@Persistent
	private int index;
	
	@Persistent
	private String fileId;
	
	
    public FileChunkEntity() {
    }
    
    public FileChunkEntity(String fileId, byte[] content, int index) {
		this();
		this.fileId = fileId;
		this.data = new Blob(content);
		this.index = index;
	}
    
    public void copy(FileChunkEntity entity) {
    	setContent(entity.getContent());
    	setFileId(entity.getFileId());
    	setIndex(entity.getIndex());
    }
    
	public byte[] getContent() {
		return data.getBytes();
	}
	
	public void setContent(byte[] content) {
		this.data = new Blob(content);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
}
