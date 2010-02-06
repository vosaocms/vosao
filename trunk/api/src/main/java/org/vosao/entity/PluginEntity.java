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

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.datanucleus.util.StringUtils;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PluginEntity implements BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;

	@Persistent
	private String name;
	
	@Persistent
	private String title;
	
	@Persistent
	private String description;
	
	@Persistent
	private String website;

	@Persistent(defaultFetchGroup = "true")
	private Text configStructure;

	@Persistent(defaultFetchGroup = "true")
	private Text configData;

	@Persistent
	private String velocityPluginClass;

	@Persistent
	private String frontServiceClass;

	@Persistent
	private String backServiceClass;

	@Persistent
	private String configURL;

	@Persistent(defaultFetchGroup = "true")
	private Text pageHeader;

	public PluginEntity() {
    }
    
    public PluginEntity(String name, String title, String configStructure,
    		String configData) {
		this();
		this.name = name;
		this.title = title;
		setConfigStructure(configStructure);
		setConfigData(configData);
	}

    @Override
	public Object getEntityId() {
		return id;
	}
    
    public void copy(PluginEntity entity) {
    	setConfigData(entity.getConfigData());
    	setConfigStructure(entity.getConfigStructure());
    	setDescription(entity.getDescription());
    	setName(entity.getName());
    	setTitle(entity.getTitle());
    	setWebsite(entity.getWebsite());
    	setVelocityPluginClass(entity.getVelocityPluginClass());
    	setFrontServiceClass(entity.getFrontServiceClass());
    	setBackServiceClass(entity.getBackServiceClass());
    	setConfigURL(entity.getConfigURL());
    	setPageHeader(entity.getPageHeader());
    }
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean equals(Object object) {
		if (object instanceof PluginEntity) {
			PluginEntity entity = (PluginEntity)object;
			if (getId() == null && entity.getId() == null) {
				return true;
			}
			if (getId() != null && getId().equals(entity.getId())) {
				return true;
			}
		}
		return false;
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
		return configStructure == null ? null : configStructure.getValue();
	}

	public void setConfigStructure(String configStructure) {
		this.configStructure = new Text(configStructure);
	}

	public String getConfigData() {
		return configData == null ? null : configData.getValue();
	}

	public void setConfigData(String configData) {
		this.configData = new Text(configData);
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
		return pageHeader == null ? null : pageHeader.getValue();
	}

	public void setPageHeader(String pageHeader) {
		this.pageHeader = new Text(pageHeader);
	}
	
}
