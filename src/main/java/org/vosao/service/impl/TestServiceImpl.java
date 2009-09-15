package org.vosao.service.impl;

import org.vosao.service.TestService;

public class TestServiceImpl implements TestService {

	@Override
	public String test(String param) {
		return param + " It works!!";
	}

}
