package org.vosao.business.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.vosao.business.PageBusiness;
import org.vosao.business.decorators.PageDecorator;
import org.vosao.entity.PageEntity;
import org.vosao.entity.TemplateEntity;

public class PageBusinessImpl extends AbstractBusinessImpl 
	implements PageBusiness {

	VelocityEngine ve;

	public void init() throws Exception {
		ve = new VelocityEngine();
		ve.init();
	}
	
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
				if (parent != null) {
					parent.getChildren().add(page);
				}
			}
		}
		return root;
	}

	@Override
	public String render(PageEntity page) {
		if (page.getTemplate() != null) {
			TemplateEntity template = getDao().getTemplateDao().getById(
					page.getTemplate());
			VelocityContext context = new VelocityContext();
			context.put("pageContent", page.getContent());
			StringWriter wr = new StringWriter();
			String log = null;
			try {
				ve.evaluate(context, wr, log, template.getContent());
				return wr.toString();
			} catch (ParseErrorException e) {
				return e.toString();
			} catch (MethodInvocationException e) {
				return e.toString();
			} catch (ResourceNotFoundException e) {
				return e.toString();
			} catch (IOException e) {
				return e.toString();
			}
		}
		else {
			return page.getContent();
		}
	}

}
