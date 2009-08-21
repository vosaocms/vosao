package org.vosao.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.vosao.test.LocalDatastoreTestCase;

public abstract class AbstractSpringTest extends LocalDatastoreTestCase {
	
    private ApplicationContext context;

	@Override
    public void setUp() throws Exception {
        super.setUp();
        initContext();
	}    
    
    private void initContext() {
        context = new ClassPathXmlApplicationContext(
                new String[] {"classpath:applicationContext.xml"});
    }

	public ApplicationContext getContext() {
		return context;
	}
	
}
