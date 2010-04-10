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

import static org.vosao.utils.EntityUtil.getStringProperty;
import static org.vosao.utils.EntityUtil.getTextProperty;
import static org.vosao.utils.EntityUtil.setProperty;
import static org.vosao.utils.EntityUtil.setTextProperty;
import static org.vosao.utils.EntityUtil.getBooleanProperty;

import com.google.appengine.api.datastore.Entity;

public class PluginEntity extends BaseEntityImpl {
	
	private static final long serialVersionUID = 3L;

	private String name;
	private String title;
	private String description;
	private String website;
	private String configStructure;
	private String configData;
	private String entryPointClass;
	private String configURL;
	private String pageHeader;
	private String version;
	private boolean disabled;

	public PluginEntity() {
		configStructure = "";
		configData = "";
		pageHeader = "";
		disabled = false;
    }
    
	@Override
	public void load(Entity entity) {
		super.load(entity);
		name = getStringProperty(entity, "name");
		title = getStringProperty(entity, "title");
		description = getStringProperty(entity, "description");
		website = getStringProperty(entity, "website");
		configStructure = getTextProperty(entity, "configStructure");
		configData = getTextProperty(entity, "configData");
		entryPointClass = getStringProperty(entity, "entryPointClass");
		configURL = getStringProperty(entity, "configURL");
		pageHeader = getTextProperty(entity, "pageHeader");
		version = getStringProperty(entity, "version");
		disabled = getBooleanProperty(entity, "disabled", false);
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		setProperty(entity, "name", name, true);
		setProperty(entity, "title", title, false);
		setProperty(entity, "description", description, false);
		setProperty(entity, "website", website, false);
		setTextProperty(entity, "configStructure", configStructure);
		setTextProperty(entity, "configData", configData);
		setProperty(entity, "entryPointClass", entryPointClass, false);
		setProperty(entity, "configURL", configURL, false);
		setTextProperty(entity, "pageHeader", pageHeader);
		setProperty(entity, "version", version, false);
		setProperty(entity, "disabled", disabled, true);
	}

	public PluginEntity(String name, String title, String configStructure,
    		String configData) {
		this();
		this.name = name;
		this.title = title;
		setConfigStructure(configStructure);
		setConfigData(configData);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getConfigStructure() {
		return configStructure;
	}

	public void setConfigStructure(String configStructure) {
		this.configStructure = configStructure;
	}

	public String getConfigData() {
		return configData;
	}

	public void setConfigData(String configData) {
		this.configData = configData;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getEntryPointClass() {
		return entryPointClass;
	}

	public void setEntryPointClass(String entryPointClass) {
		this.entryPointClass = entryPointClass;
	}
	
	public String getConfigURL() {
		return configURL;
	}

	public void setConfigURL(String configURL) {
		this.configURL = configURL;
	}

	public String getPageHeader() {
		return pageHeader;
	}

	public void setPageHeader(String pageHeader) {
		this.pageHeader = pageHeader;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

}
