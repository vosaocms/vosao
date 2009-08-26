package org.vosao.business;

import java.util.List;

import org.vosao.entity.PageEntity;


public interface PageBusiness {

	PageDecorator getTree(final List<PageEntity> pages);
	
}
