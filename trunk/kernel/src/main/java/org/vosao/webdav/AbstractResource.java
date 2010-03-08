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

package org.vosao.webdav;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.common.BCrypt;
import org.vosao.dao.Dao;
import org.vosao.entity.UserEntity;

import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.PropFindableResource;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.Request.Method;

public class AbstractResource implements Resource, PropFindableResource {

	protected static final Log logger = LogFactory.getLog(
			AbstractResource.class);
	
	private Business business;
	private String name;
	private Date modDate;
	
	public AbstractResource(Business aBusiness, String aName, Date aModDate) {
		business = aBusiness;
		name = aName;
		modDate = aModDate;
	}
	
	@Override
	public Object authenticate(String username, String password) {
		UserEntity user = getDao().getUserDao().getByEmail(username);
		if (user != null) {
			if (BCrypt.checkpw(password, user.getPassword())) {
				return user;
			}
		}
		return null;
	}

	@Override
	public boolean authorise(Request request, Method method, Auth auth) {
		if (auth != null) {
			return true;
		}
		return false;
	}

	@Override
	public String checkRedirect(Request arg0) {
		return null;
	}

	@Override
	public Date getModifiedDate() {
		return modDate;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getRealm() {
		return "vosao";
	}

	@Override
	public String getUniqueId() {
		return name;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public Dao getDao() {
		return getBusiness().getDao();
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setModifiedDate(Date modDate) {
		this.modDate = modDate;
	}

	@Override
	public Date getCreateDate() {
		return modDate;
	}

}
