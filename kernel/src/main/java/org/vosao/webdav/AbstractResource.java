package org.vosao.webdav;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.common.BCrypt;
import org.vosao.dao.Dao;
import org.vosao.entity.UserEntity;

import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.Request.Method;

public class AbstractResource implements Resource {

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

}
