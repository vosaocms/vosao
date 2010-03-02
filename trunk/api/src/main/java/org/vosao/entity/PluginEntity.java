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

import org.datanucleus.util.StringUtils;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

public class PluginEntity extends BaseEntityImpl {
	
	private static final long serialVersionUID = 3L;

	private String name;
	private String title;
	private String description;
	private String website;
	private String configStructure;
	private String configData;
	private String velocityPluginClass;
	private String frontServiceClass;
	private String backServiceClass;
	private String configURL;
	private String pageHeader;

	public PluginEntity() {
		name = "";
		title = "";
		description = "";
		website = "";
		configStructure = "";
		configData = "";
		velocityPluginClass = "";
		frontServiceClass = "";
		backServiceClass = "";
		configURL = "";
		pageHeader = "";
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
		velocityPluginClass = getStringProperty(entity, "velocityPluginClass");
		frontServiceClass = getStringProperty(entity, "frontServiceClass");
		backServiceClass = getStringProperty(entity, "backServiceClass");
		configURL = getStringProperty(entity, "configURL");
		pageHeader = getTextProperty(entity, "pageHeader");
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		entity.setProperty("name", name);
		entity.setProperty("title", title);
		entity.setProperty("description", description);
		entity.setProperty("website", website);
		entity.setProperty("configStructure", new Text(configStructure));
		entity.setProperty("configData", new Text(configData));
		entity.setProperty("velocityPluginClass", velocityPluginClass);
		entity.setProperty("frontServiceClass", frontServiceClass);
		entity.setProperty("backServiceClass", backServiceClass);
		entity.setProperty("configURL", configURL);
		entity.setProperty("pageHeader", new Text(pageHeader));
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

	public String getVelocityPluginClass() {
		return velocityPluginClass;
	}

	public void setVelocityPluginClass(String velocityPluginClass) {
		this.velocityPluginClass = velocityPluginClass;
	}
	
	public boolean isVelocityPlugin() {
		return !StringUtils.isEmpty(velocityPluginClass);
	}

	public boolean isFrontServicePlugin() {
		return !StringUtils.isEmpty(frontServiceClass);
	}

	public boolean isBackServicePlugin() {
		return !StringUtils.isEmpty(backServiceClass);
	}
	
	public String getFrontServiceClass() {
		return frontServiceClass;
	}

	public void setFrontServiceClass(String frontServiceClass) {
		this.frontServiceClass = frontServiceClass;
	}

	public String getBackServiceClass() {
		return backServiceClass;
	}

	public void setBackServiceClass(String backServiceClass) {
		this.backServiceClass = backServiceClass;
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
	
}
