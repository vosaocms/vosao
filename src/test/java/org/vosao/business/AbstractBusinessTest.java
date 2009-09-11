package org.vosao.business;

import org.vosao.dao.Dao;
import org.vosao.test.AbstractSpringTest;

public abstract class AbstractBusinessTest extends AbstractSpringTest {
	
    private Dao dao;
    private Business business;

	@Override
    public void setUp() throws Exception {
        super.setUp();
        dao = (Dao)getContext().getBean("dao");
        business = (Business)getContext().getBean("business");
	}    
    
	public Dao getDao() {
		return dao;
	}

	public Business getBusiness() {
		return business;
	}
	
}
