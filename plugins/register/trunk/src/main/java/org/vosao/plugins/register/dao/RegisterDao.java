package org.vosao.plugins.register.dao;

import org.vosao.dao.Dao;

public class RegisterDao {

	private RegistrationDao registrationDao;
	private Dao dao;

	public RegisterDao(Dao aDao) {
		setDao(aDao);
	}
	
	public void setDao(Dao aDao) {
		dao = aDao;
	}
	
	public Dao getDao() {
		return dao;
	}

	public RegistrationDao getRegistrationDao() {
		if (registrationDao == null) {
			registrationDao = new RegistrationDaoImpl(getDao().getEntityCache(), 
					getDao().getQueryCache());
		}
		return registrationDao;
	}
	
}
