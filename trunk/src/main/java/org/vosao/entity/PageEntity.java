package org.vosao.entity;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PageEntity implements Serializable {

	private static final long serialVersionUID = 6L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;
	
	@Persistent
	private String title;
	
	@Persistent(defaultFetchGroup = "true")
	private Text content;
	
	@Persistent
	private String friendlyURL;
	
	@Persistent
	private String parent;
	
	@Persistent
	private String template;
	
	public PageEntity() {
	}
	
	public PageEntity(String title, String content,
			String friendlyURL, String aParent, String aTemplate) {
		this(title, content, friendlyURL, aParent);
		template = aTemplate;
	}

	public PageEntity(String title, String content,
			String friendlyURL, String aParent) {
		this();
		this.title = title;
		this.content = new Text(content);
		this.friendlyURL = friendlyURL;
		this.parent = aParent;
	}
	
	public void copy(final PageEntity page) {
		setTitle(page.getTitle());
		setContent(page.getContent());
		setFriendlyURL(page.getFriendlyURL());
		setParent(page.getParent());
		setTemplate(page.getTemplate());
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

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
	public String getParentFriendlyURL() {
		if (getFriendlyURL() == null) {
			return "";
		}
		int lastSlash = getFriendlyURL().lastIndexOf('/');
		if (lastSlash == 0 || lastSlash == -1) {
			return "";
		}
		return getFriendlyURL().substring(0, lastSlash);
	}

	public void setParentFriendlyURL(final String url) {
		if (getFriendlyURL() == null) {
			setFriendlyURL(url);
		}
		else {
			setFriendlyURL(url + "/" + getPageFriendlyURL());
		}
	}

	public String getPageFriendlyURL() {
		if (getFriendlyURL() == null) {
			return "";
		}
		int lastSlash = getFriendlyURL().lastIndexOf('/');
		if (getFriendlyURL().equals("/") || lastSlash == -1) {
			return "";
		}
		return getFriendlyURL().substring(lastSlash + 1, getFriendlyURL().length());
	}

	public void setPageFriendlyURL(final String url) {
		if (getFriendlyURL() == null) {
			setFriendlyURL(url);
		}
		else {
			setFriendlyURL(getParentFriendlyURL() + "/" + url);
		}
	}

}
