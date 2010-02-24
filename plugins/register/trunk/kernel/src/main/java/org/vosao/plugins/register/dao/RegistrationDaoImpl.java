package org.vosao.plugins.register.dao;

import org.vosao.dao.impl.BaseDaoImpl;
import org.vosao.plugins.register.entity.RegistrationEntity;

public class RegistrationDaoImpl extends BaseDaoImpl<Long, RegistrationEntity> 
		implements RegistrationDao {

	public RegistrationDaoImpl() {
		super(RegistrationEntity.class);
	}
	
}
