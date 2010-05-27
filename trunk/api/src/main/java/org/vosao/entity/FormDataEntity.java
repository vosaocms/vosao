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

package org.vosao.entity;

import static org.vosao.utils.EntityUtil.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.google.appengine.api.datastore.Entity;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class FormDataEntity extends BaseEntityImpl {
	
	private static final long serialVersionUID = 1L;

	private String uuid;
	private Long formId;
	private String ipAddress;

	// for data stored as XML
	private String data;
	
    public FormDataEntity() {
    }
    
    public FormDataEntity(Long formId, String data) {
		this();
		this.formId = formId;
		this.data = data;
	}

    @Override
    public void load(Entity entity) {
    	super.load(entity);
    	formId = getLongProperty(entity, "formId");
    	data = getTextProperty(entity, "data");
    	ipAddress = getStringProperty(entity, "ipAddress");
    }
    
    @Override
    public void save(Entity entity) {
    	super.save(entity);
    	setProperty(entity, "formId", formId, true);
    	setTextProperty(entity, "data", data);
    	setProperty(entity, "ipAddress", ipAddress, false);
    }

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public Map<String, String> getValues() {
		Map<String, String> result = new HashMap<String, String>();
		try {
			Document doc = DocumentHelper.parseText(getData());
			for (Element e : (List<Element>)doc.getRootElement().elements()) {
				result.put(e.getName(), e.getText());
			}
		}
		catch (DocumentException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
}
