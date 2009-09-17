package org.vosao.service;

import java.util.Map;


public interface FormService extends AbstractService {
	
	ServiceResponse send(final String name, Map<String, String> params);
	
}
