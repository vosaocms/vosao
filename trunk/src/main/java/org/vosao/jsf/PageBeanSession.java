package org.vosao.jsf;
import java.io.Serializable;


public class PageBeanSession implements Serializable {

	private static final long serialVersionUID = 2L;
	
	private String parent;
	
	public PageBeanSession () {
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
	
}
