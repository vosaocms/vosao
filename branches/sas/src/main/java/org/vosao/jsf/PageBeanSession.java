package org.vosao.jsf;
import java.io.Serializable;


public class PageBeanSession implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean edit;
	
	public PageBeanSession () {
		edit = false;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}
	
	
}
