package org.vosao.entity;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PageEntity implements Serializable {

	private static final long serialVersionUID = 4L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	
	@Persistent
	private String title;
	
	@Persistent(defaultFetchGroup = "true")
	private Text content;
	
	@Persistent
	private String friendlyURL;
	
	@Persistent
	private Long parent;
	
	public PageEntity() {
	}
	
	public PageEntity(String title, String content,
			String friendlyURL, Long parent) {
		this();
		this.title = title;
		this.content = new Text(content);
		this.friendlyURL = friendlyURL;
		this.parent = parent;
	}
	
	public void copy(final PageEntity page) {
		setTitle(page.getTitle());
		setContent(page.getContent());
		setFriendlyURL(page.getFriendlyURL());
		setParent(page.getParent());		
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
	
	public String getContent() {
		if (content == null) {
			return null;
		}
		return content.getValue();
	}
	
	public void setContent(String content) {
		this.content = new Text(content);
	}
	
	public String getFriendlyURL() {
		return friendlyURL;
	}
	
	public void setFriendlyURL(String friendlyURL) {
		this.friendlyURL = friendlyURL;
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}
}
