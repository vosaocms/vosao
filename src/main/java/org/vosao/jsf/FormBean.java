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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.entity.FormConfigEntity;
import org.vosao.entity.FormEntity;


public class FormBean extends AbstractJSFBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(FormBean.class);
	
	private List<FormEntity> list;
	private FormEntity current;
	private Map<String, Boolean> selected;
	private String id;
	private FormConfigEntity formConfig;

	public void init() {
		initList();
		current = new FormEntity();
		initSelected();
		initFormConfig();
	}
	
	private void initList() {
		list = getDao().getFormDao().select();
	}
	
	private void initSelected() {
		selected = new HashMap<String, Boolean>();
		for (FormEntity Form : list) {
			selected.put(Form.getId(), false);
		}
	}
	
	private void initFormConfig() {
		formConfig = getDao().getFormDao().getConfig();
	}
	
	public String cancelEdit() {
		return "pretty:forms";
	}
	
	public String update() {
		List<String> errors = getBusiness().getFormBusiness()
				.validateBeforeUpdate(current);
		if (errors.isEmpty()) {
			getDao().getFormDao().save(current);
			list.add(current);
			return "pretty:forms";
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
		getDao().getFormDao().remove(ids);
		initList();
		return "pretty:forms";
	}
	
	public void edit() {
		if (id != null) {
			current = getDao().getFormDao().getById(id);
		}
	}
	
	public void list() {
	}
	
	public String add() {
		return "pretty:formCreate";
	}
	
	public void saveConfig() {
		getDao().getFormDao().save(formConfig);
		JSFUtil.addInfoMessage("Form configuration was successfully saved.");
	}
	
	public List<FormEntity> getList() {
		return list;
	}
	
	public boolean isEdit() {
		return current.getId() != null;
	}

	public FormEntity getCurrent() {
		return current;
	}

	public void setCurrent(FormEntity current) {
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

	public FormConfigEntity getFormConfig() {
		return formConfig;
	}

	public void setFormConfig(FormConfigEntity formConfig) {
		this.formConfig = formConfig;
	}
	
}
