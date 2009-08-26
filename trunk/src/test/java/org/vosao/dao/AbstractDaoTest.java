package org.vosao.dao;

import org.vosao.test.AbstractSpringTest;

public abstract class AbstractDaoTest extends AbstractSpringTest {
	
    private Dao dao;

	@Override
    public void setUp() throws Exception {
        super.setUp();
        dao = (Dao)getContext().getBean("dao");
	}    
    
	public Dao getDao() {
		return dao;
	}
	
}
