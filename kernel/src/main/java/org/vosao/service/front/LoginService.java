package org.vosao.service.front;

import javax.servlet.http.HttpServletRequest;

import org.vosao.service.AbstractService;
import org.vosao.service.ServiceResponse;

public interface LoginService extends AbstractService {

	ServiceResponse login(final String email, final String password,
			HttpServletRequest request);
	
	ServiceResponse logout(HttpServletRequest request);
}
