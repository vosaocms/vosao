package org.vosao.business.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vosao.business.PageBusiness;
import org.vosao.business.PageDecorator;
import org.vosao.entity.PageEntity;

public class PageBusinessImpl extends AbstractBusinessImpl 
	implements PageBusiness {

	@Override
	public PageDecorator getTree(final List<PageEntity> pages) {
		Map<String, PageDecorator> buf = new HashMap<String, PageDecorator>();
		for (PageEntity page : pages) {
			buf.put(page.getId(), new PageDecorator(page));
		}
		PageDecorator root = null;
		for (String id : buf.keySet()) {
			PageDecorator page = buf.get(id);
			if (page.getPage().getParent() == null) {
				root = page;
			}
			else {
				PageDecorator parent = buf.get(page.getPage().getParent());
				parent.getChildren().add(page);
			}
		}
		return root;
	}

}
