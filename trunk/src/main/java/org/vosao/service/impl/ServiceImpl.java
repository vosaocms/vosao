package org.vosao.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jabsorb.JSONRPCBridge;
import org.vosao.service.Service;
import org.vosao.service.TestService;

public class ServiceImpl implements Service {

	private static final Log log = LogFactory.getLog(ServiceImpl.class);

	private TestService testService;
	
	public void init() {
		JSONRPCBridge.getGlobalBridge().registerObject("testService", testService);
	}
	
	@Override
	public TestService getTestService() {
		return testService;
	}

	@Override
	public void setTestService(TestService bean) {
		testService = bean;		
	}

}
