package org.vosao.business.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.vosao.business.PageBusiness;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.PageEntity;
import org.vosao.entity.TemplateEntity;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

public class PageBusinessImpl extends AbstractBusinessImpl 
	implements PageBusiness {

	VelocityEngine ve;

	public void init() throws Exception {
		ve = new VelocityEngine();
		ve.init();
	}
	
	@Override
	public TreeItemDecorator<PageEntity> getTree(final List<PageEntity> pages) {
		Map<String, TreeItemDecorator<PageEntity>> buf = 
				new HashMap<String, TreeItemDecorator<PageEntity>>();
		for (PageEntity page : pages) {
			buf.put(page.getId(), new TreeItemDecorator<PageEntity>(page, null));
		}
		TreeItemDecorator<PageEntity> root = null;
		for (String id : buf.keySet()) {
			TreeItemDecorator<PageEntity> page = buf.get(id);
			if (page.getEntity().getParent() == null) {
				root = page;
			}
			else {
				TreeItemDecorator<PageEntity> parent = buf.get(page.getEntity()
						.getParent());
				if (parent != null) {
					parent.getChildren().add(page);
					page.setParent(parent);
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
			context.put("pageTitle", page.getTitle());
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
	
	@Override
	public List<String> validateBeforeUpdate(final PageEntity page) {
		List<String> errors = new ArrayList<String>();
		if (page.getId() == null) {
			PageEntity myPage = getDao().getPageDao().getByUrl(
					page.getFriendlyURL());
			if (myPage != null) {
				errors.add("Page with such friendly URL already exists");
			}
		}
		if (StringUtil.isEmpty(page.getFriendlyURL())) {
			errors.add("Friendly URL is empty");
		}
		if (!page.getFriendlyURL().equals("/") 
			&& StringUtil.isEmpty(page.getPageFriendlyURL())) {
			errors.add("Friendly URL is empty");
		}
		if (StringUtil.isEmpty(page.getTitle())) {
			errors.add("Title is empty");
		}
		if (StringUtil.isEmpty(page.getContent())) {
			errors.add("Content is empty");
		}
		return errors;
	}

	@Override
	public TreeItemDecorator<PageEntity> getTree() {
		return getTree(getDao().getPageDao().select());
	}

}
