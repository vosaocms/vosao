package org.vosao.entity;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PageEntity {
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private String title;
	
	@Persistent
	private String content;
	
	@Persistent
	private String friendlyURL;
	
	//@Persistent
	//private PageEntity parent;
	
	//@Persistent(mappedBy = "parent")
	//private List<PageEntity> children;
	
	public PageEntity() {
		//children = new ArrayList<PageEntity>();
	}
	
	public PageEntity(String title, String content,
			String friendlyURL, PageEntity parent) {
		this();
		this.title = title;
		this.content = content;
		this.friendlyURL = friendlyURL;
		//this.parent = parent;
	}
	
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFriendlyURL() {
		return friendlyURL;
	}
	public void setFriendlyURL(String friendlyURL) {
		this.friendlyURL = friendlyURL;
	}
	/*public PageEntity getParent() {
		return parent;
	}
	public void setParent(PageEntity parent) {
		this.parent = parent;
	}
	public List<PageEntity> getChildren() {
		return children;
	}
	public void setChildren(List<PageEntity> children) {
		this.children = children;
	}*/
}
