package org.vosao.business;

import java.util.List;

import org.vosao.business.decorators.PageDecorator;
import org.vosao.entity.PageEntity;


public interface PageBusiness {

	PageDecorator getTree(final List<PageEntity> pages);
	
	String render(final PageEntity page);
	
	List<String> validateBeforeUpdate(final PageEntity page);
}
