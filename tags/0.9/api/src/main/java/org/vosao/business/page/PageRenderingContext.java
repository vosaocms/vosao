package org.vosao.business.page;

import java.util.HashSet;
import java.util.Set;

import org.vosao.entity.PageEntity;

public class PageRenderingContext {

	private PageEntity page;
	private Set<String> headContents = new HashSet<String>();
	
	public PageEntity getPage() {
		return page;
	}

	public void setPage(PageEntity page) {
		this.page = page;
	}

	public Set<String> getHeadContents() {
		return headContents;
	}
	
	public void clear() {
		page = null;
		headContents.clear();
	}
	
}
