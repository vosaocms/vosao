package org.vosao.business;

import java.util.ArrayList;
import java.util.List;

import org.vosao.entity.PageEntity;

public class PageDecorator {

	private PageEntity page;
	private List<PageDecorator> children;
	
	public PageDecorator(PageEntity page) {
		super();
		this.page = page;
		this.children = new ArrayList<PageDecorator>();
	}

	public PageDecorator(PageEntity page, List<PageDecorator> children) {
		super();
		this.page = page;
		this.children = children;
	}
	
	public PageEntity getPage() {
		return page;
	}
	public List<PageDecorator> getChildren() {
		return children;
	}
	
	
}
