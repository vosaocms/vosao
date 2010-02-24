package org.vosao.plugins.register.dao;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class RegisterDao {

	private static PersistenceManagerFactory pmf;
	
	private static RegistrationDao registrationDao;
	
	public static PersistenceManagerFactory getPMF() {
		if (pmf == null) {
			pmf = JDOHelper.getPersistenceManagerFactory(
				"transactions-optional", RegisterDao.class.getClassLoader());
		}
		return pmf;
	}
	
	public static RegistrationDao getRegistrationDao() {
		if (registrationDao == null) {
			registrationDao = new RegistrationDaoImpl();
			registrationDao.setPersistenceManagerFactory(getPMF());
		}
		return registrationDao;
	}
	
}
