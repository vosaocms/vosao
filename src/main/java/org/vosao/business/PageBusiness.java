package org.vosao.business;

import java.util.List;

import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.PageEntity;


public interface PageBusiness {

	TreeItemDecorator<PageEntity> getTree(final List<PageEntity> pages);
	
	TreeItemDecorator<PageEntity> getTree();

	String render(final PageEntity page);
	
	List<String> validateBeforeUpdate(final PageEntity page);
}
