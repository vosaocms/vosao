package org.vosao.service.front.impl;

import javax.servlet.http.HttpServletRequest;

import org.vosao.business.UserPreferences;
import org.vosao.entity.UserEntity;
import org.vosao.filter.AuthenticationFilter;
import org.vosao.service.ServiceResponse;
import org.vosao.service.front.LoginService;
import org.vosao.service.impl.AbstractServiceImpl;

public class LoginServiceImpl extends AbstractServiceImpl 
		implements LoginService {

	@Override
	public ServiceResponse login(String email, String password,
			HttpServletRequest request) {
		
		UserEntity user = getDao().getUserDao().getByEmail(email);
		if (user == null) {
			return ServiceResponse.createErrorResponse("User was not found.");
		}
		if (!user.getPassword().equals(password)) {
			return ServiceResponse.createErrorResponse("Password incorrect.");
		}
		UserPreferences userPreferences = getBusiness().getUserPreferences(
				request);
		userPreferences.setUser(user);
		String originalView = (String) request.getSession().getAttribute(
				AuthenticationFilter.ORIGINAL_VIEW_KEY);
		request.getSession().removeAttribute(
				AuthenticationFilter.ORIGINAL_VIEW_KEY);
		if (originalView.equals("/login.jsp")) {
			originalView = "/cms/index.jsp";
		}
		return ServiceResponse.createSuccessResponse(originalView);
	}
	

}
