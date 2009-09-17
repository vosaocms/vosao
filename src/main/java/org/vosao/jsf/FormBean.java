package org.vosao.jsf;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.entity.FormEntity;


public class FormBean extends AbstractJSFBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(FormBean.class);
	
	private List<FormEntity> list;
	private FormEntity current;
	private Map<String, Boolean> selected;
	private String id;

	public void init() {
		initList();
		current = new FormEntity();
		initSelected();
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
	
}
