package org.vosao.entity;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mapsto.AbstractEntity;
import org.vosao.utils.DateUtil;

public abstract class BaseMapstoEntityImpl extends AbstractEntity 
		implements BaseMapstoEntity {

	protected static final Log logger = LogFactory.getLog(
			BaseMapstoEntityImpl.class);

	private String createUserEmail;
	private Date createDate;
	private String modUserEmail;
	private Date modDate;

	public BaseMapstoEntityImpl() {
		createDate = new Date();
		modDate = createDate;
		createUserEmail = "";
		modUserEmail = "";
	}
	
	@Override
	public boolean isNew() {
		return getId() == null;
	}
	
	public boolean equals(Object object) {
		if (object instanceof BaseMapstoEntity
				&& object.getClass().equals(this.getClass())) {
			BaseMapstoEntity entity = (BaseMapstoEntity) object;
			if (getId() == null && entity.getId() == null) {
				return true;
			}
			if (getId() != null && getId().equals(entity.getId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getCreateUserEmail() {
		return createUserEmail;
	}

	@Override
	public void setCreateUserEmail(String createUserEmail) {
		this.createUserEmail = createUserEmail;
	}

	@Override
	public Date getCreateDate() {
		return createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String getModUserEmail() {
		return modUserEmail;
	}

	@Override
	public void setModUserEmail(String modUserEmail) {
		this.modUserEmail = modUserEmail;
	}

	@Override
	public Date getModDate() {
		return modDate;
	}

	@Override
	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	@Override
	public String getModDateString() {
		return DateUtil.toString(modDate);
	}

	@Override
	public String getCreateDateString() {
		return DateUtil.toString(createDate);
	}
	
}
