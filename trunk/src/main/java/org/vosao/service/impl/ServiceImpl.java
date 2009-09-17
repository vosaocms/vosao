package org.vosao.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jabsorb.JSONRPCBridge;
import org.vosao.service.FormService;
import org.vosao.service.Service;
import org.vosao.service.TestService;

public class ServiceImpl implements Service {

	private static final Log log = LogFactory.getLog(ServiceImpl.class);

	private TestService testService;
	private FormService formService;
	
	public void init() {
		JSONRPCBridge.getGlobalBridge().registerObject("testService", testService);
		JSONRPCBridge.getGlobalBridge().registerObject("formService", formService);
	}
	
	@Override
	public TestService getTestService() {
		return testService;
	}

	@Override
	public void setTestService(TestService bean) {
		testService = bean;		
	}

	@Override
	public FormService getFormService() {
		return formService;
	}

	@Override
	public void setFormService(FormService bean) {
		formService = bean;
	}

}
