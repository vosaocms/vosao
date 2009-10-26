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
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.entity.TemplateEntity;
import org.vosao.servlet.MimeType;


public class TemplateBean extends AbstractJSFBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(TemplateBean.class);
	
	private List<TemplateEntity> list;
	private TemplateEntity current;
	private Map<String, Boolean> selected;
	private String id;

	public void init() {
		initList();
		current = new TemplateEntity();
		initSelected();
	}
	
	private void initList() {
		list = getDao().getTemplateDao().select();
	}
	
	private void initSelected() {
		selected = new HashMap<String, Boolean>();
		for (TemplateEntity Template : list) {
			selected.put(Template.getId(), false);
		}
	}
	
	public String cancelEdit() {
		return "pretty:templates";
	}
	
	public String update() {
		List<String> errors = getBusiness().getTemplateBusiness()
				.validateBeforeUpdate(current);
		if (errors.isEmpty()) {
			getDao().getTemplateDao().save(current);
			list.add(current);
			return "pretty:templates";
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
		getDao().getTemplateDao().remove(ids);
		initList();
		return "pretty:templates";
	}
	
	public void edit() {
		if (id != null) {
			current = getDao().getTemplateDao().getById(id);
		}
	}
	
	public void list() {
	}
	
	public String add() {
		return "pretty:templateCreate";
	}
	
	public void export() throws IOException {
		List<TemplateEntity> selectedTemplates = new ArrayList<TemplateEntity>();
		for (String id : selected.keySet()) {
			if (selected.get(id)) {
				selectedTemplates.add(getDao().getTemplateDao().getById(id));
			}
		}
		if (selectedTemplates.size() == 0) {
			JSFUtil.addInfoMessage("Nothing selected for export");
			return;
		}
		HttpServletResponse response = JSFUtil.getResponse();
		response.setContentType(MimeType.getContentTypeByExt("zip"));
		String downloadFile = "exportTheme.zip";
		response.addHeader("Content-Disposition", "attachment; filename=\"" 
				+ downloadFile + "\"");
		ServletOutputStream out = response.getOutputStream();
		byte[] file = getBusiness().getImportExportBusiness().createExportFile(
				selectedTemplates);
		response.setContentLength(file.length);
		out.write(file);
		out.flush();
		out.close();
		FacesContext.getCurrentInstance().responseComplete();
	}
	
	public List<TemplateEntity> getList() {
		return list;
	}
	
	public boolean isEdit() {
		return current.getId() != null;
	}

	public TemplateEntity getCurrent() {
		return current;
	}

	public void setCurrent(TemplateEntity current) {
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
	
}
