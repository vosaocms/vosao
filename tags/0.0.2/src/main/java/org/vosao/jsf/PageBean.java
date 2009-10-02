/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * Copyright (C) 2009 Vosao development team
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.CommentEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.utils.DateUtil;

public class PageBean extends AbstractJSFBean implements Serializable {

	public static final String IMAGE_UPLOAD_PAGE_ID = "imageUploadPageId";
	
	private static final long serialVersionUID = 2L;
	private static Log log = LogFactory.getLog(PageBean.class);
	
	private List<PageEntity> list;
	private PageEntity current;
	private Map<String, Boolean> selected;
	private String id;
	private TreeItemDecorator<PageEntity> root;
	private List<PageEntity> children;
	private List<SelectItem> templates;
	private String publishDate;
	private Date dPublishDate;
	private List<CommentEntity> comments;
	private Map<String, Boolean> selectedComments;

	public void init() {
		initList();
		current = new PageEntity();
		current.setContent("");
		publishDate = DateUtil.toString(current.getPublishDate());
		if (getParentURL() == null || getParentURL().equals("/")) {
			current.setFriendlyURL("/");
		}
		else {
			current.setFriendlyURL(getParentURL() + "/");
		}
		initSelected();
		initDecorator();
		initTemplates();
		initComments();
	}
	
	private void initTemplates() {
		List<TemplateEntity> templateList = getDao().getTemplateDao().select();
		templates = new ArrayList<SelectItem>();
		for (TemplateEntity t : templateList) {
			templates.add(new SelectItem(t.getId(), t.getTitle()));
		}
	}
	
	private void initList() {
		list = getDao().getPageDao().select();
	}
	
	private void initDecorator() {
		root = getBusiness().getPageBusiness().getTree(list);
	}

	private void initSelected() {
		selected = new HashMap<String, Boolean>();
		for (PageEntity page : list) {
			selected.put(page.getId(), false);
		}
	}
	
	private void initChildren() {
		if (current != null) {
			children = getDao().getPageDao().getByParent(current.getId());
		}
	}
	
	private void initComments() {
		comments = getDao().getCommentDao().getByPage(current.getId()); 
		selectedComments = new HashMap<String, Boolean>();
		for (CommentEntity comment : comments) {
			selectedComments.put(comment.getId(), false);
		}
	}
	
	public String cancelEdit() {
		return "pretty:pages";
	}
	
	private List<String> validate() {
		List<String> result = new ArrayList<String>();
		try {
			dPublishDate = DateUtil.toDate(publishDate);
		}
		catch (ParseException e) {
			result.add("Date has a wrong format (must be DD.MM.YYYY)");
		}
		return result;
	}
	
	public String update() {
		List<String> errors = getBusiness().getPageBusiness()
			.validateBeforeUpdate(current);
		errors.addAll(validate());
		if (errors.isEmpty()) {
			current.setPublishDate(dPublishDate);
			if (current.getId() == null) {
				current.setParent(getParent());
			}
			getDao().getPageDao().save(current);
			list.add(current);
			initDecorator();
			return "pretty:pages";
		}
		else {
			JSFUtil.addErrorMessages(errors);
			return null;
		}
	}
	
	public String delete() {
		List<String> ids = new ArrayList<String>();
		for (String id : selected.keySet()) {
			if (selected.get(id)) {
				ids.add(id);
			}
		}
		getDao().getPageDao().remove(ids);
		initList();
		return "pretty:pages";
	}
	
	public void edit() {
		if (id != null) {
			current = getDao().getPageDao().getById(id);
			publishDate = DateUtil.toString(current.getPublishDate());
			initChildren();
			initComments();
			setImageUploadPageId(id);
		}
	}
	
	public void list() {
	}
	
	public String addChild() {
		setParent(current.getId());
		setParentURL(current.getFriendlyURL());
		return "pretty:pageCreate";
	}
	
	public void preview() throws IOException {
		JSFUtil.redirect(current.getFriendlyURL());
	}
	
	public String addChildParam() {
		if (id != null) {
			current = getDao().getPageDao().getById(id);
			initChildren();
		}
		setParent(current.getId());
		setParentURL(current.getFriendlyURL());
		return "pretty:pageCreate";
	}
	
	public void enableComments() {
		getDao().getCommentDao().enable(getSelectedCommentsIds());
		initComments();
	}
	
	public void disableComments() {
		getDao().getCommentDao().disable(getSelectedCommentsIds());
		initComments();
	}

	public void deleteComments() {
		getDao().getCommentDao().remove(getSelectedCommentsIds());
		initComments();
	}
	
	private List<String> getSelectedCommentsIds() {
		List<String> ids = new ArrayList<String>();
		for (String id : selectedComments.keySet()) {
			if (selectedComments.get(id)) {
				ids.add(id);
			}
		}
		return ids;
	}
	
	public List<PageEntity> getList() {
		return list;
	}
	
	public boolean isEdit() {
		return current.getId() != null;
	}

	public PageEntity getCurrent() {
		return current;
	}

	public void setCurrent(PageEntity current) {
		this.current = current;
	}

	public Map<String, Boolean> getSelected() {
		return selected;
	}

	public void setSelected(Map<String, Boolean> selected) {
		this.selected = selected;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTree() {
		if (root != null) {
			return renderPageTree(root).toString();
		}
		else {
			return "empty tree";
		}
	}
	
	private static StringBuffer renderPageTree(
			final TreeItemDecorator<PageEntity> page) {
		StringBuffer result = new StringBuffer();
		String editPageLink = "<a href=\"page/edit/" + page.getEntity().getId()
			+ "\">" + page.getEntity().getTitle() + "</a>";
		String addChildParamLink = "&nbsp;<a title=\"Add child\" href=\"/cms/page/createChild/"
			+ page.getEntity().getId() + "\">+</a>";
		
		result.append("<li>").append(editPageLink).append(addChildParamLink);
		if (page.getChildren().size() > 0) {
			result.append("<ul>");
		}
		for (TreeItemDecorator<PageEntity> child : page.getChildren()) {
			result.append(renderPageTree(child));
		}
		if (page.getChildren().size() > 0) {
			result.append("</ul>");
		}
		result.append("</li>");
		return result;
	}

	public List<PageEntity> getChildren() {
		return children;
	}

	public void setChildren(List<PageEntity> children) {
		this.children = children;
	}
	
	public boolean isShowChildren() {
		return children != null;
	}

	public String getParent() {
		String name = this.getClass().getName() + "parent";
		return (String)JSFUtil.getSessionObject(name);
	}

	public void setParent(String parent) {
		String name = this.getClass().getName() + "parent";
		JSFUtil.setSessionObject(name, parent);
	}

	public String getParentURL() {
		String name = this.getClass().getName() + "parentURL";
		return (String)JSFUtil.getSessionObject(name);
	}

	public void setParentURL(String parent) {
		String name = this.getClass().getName() + "parentURL";
		JSFUtil.setSessionObject(name, parent);
	}

	public void setImageUploadPageId(String id) {
		JSFUtil.setSessionObject(IMAGE_UPLOAD_PAGE_ID, id);
	}

	public List<SelectItem> getTemplates() {
		return templates;
	}

	public void setTemplates(List<SelectItem> templates) {
		this.templates = templates;
	}
	
	public boolean isEditURL() {
		return !isEdit() || (children != null && children.size() == 0); 
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String aPublishDate) {
		publishDate = aPublishDate;
	}

	public List<CommentEntity> getComments() {
		return comments;
	}

	public void setComments(List<CommentEntity> comments) {
		this.comments = comments;
	}

	public Map<String, Boolean> getSelectedComments() {
		return selectedComments;
	}

	public void setSelectedComments(Map<String, Boolean> selectedComments) {
		this.selectedComments = selectedComments;
	}
}
