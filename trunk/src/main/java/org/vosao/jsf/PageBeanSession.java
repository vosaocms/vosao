package org.vosao.jsf;
import java.io.Serializable;


public class PageBeanSession implements Serializable {

	private static final long serialVersionUID = 2L;
	
	private boolean edit;
	private boolean newEntity;
	private String parent;
	
	public PageBeanSession () {
		edit = false;
		newEntity = true;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public boolean isNewEntity() {
		return newEntity;
	}

	public void setNewEntity(boolean newEntity) {
		this.newEntity = newEntity;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
	
	
}
