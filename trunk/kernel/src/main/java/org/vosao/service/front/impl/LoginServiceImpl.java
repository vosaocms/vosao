package org.vosao.service.front.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.vosao.common.BCrypt;
import org.vosao.common.Messages;
import org.vosao.entity.UserEntity;
import org.vosao.filter.AuthenticationFilter;
import org.vosao.service.ServiceResponse;
import org.vosao.service.front.LoginService;
import org.vosao.service.impl.AbstractServiceImpl;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class LoginServiceImpl extends AbstractServiceImpl 
		implements LoginService {

	@Override
	public ServiceResponse login(String email, String password,
			HttpServletRequest request) {
		
		UserEntity user = getDao().getUserDao().getByEmail(email);
		if (user == null) {
			return ServiceResponse.createErrorResponse(Messages.get(
					"user_not_found"));
		}
		ServiceResponse passwordIncorrect = ServiceResponse.createErrorResponse(
				Messages.get("password_incorrect"));
		if (user.getPassword() == null) {
			if (!StringUtils.isEmpty(password)) {
				return passwordIncorrect;
			}
		}
		else {		
			try {
				if (!BCrypt.checkpw(password, user.getPassword())) {
					return passwordIncorrect;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				return passwordIncorrect;
			}
		}
		HttpSession session = request.getSession(true);
		session.setAttribute(AuthenticationFilter.USER_SESSION_ATTR, 
				user.getEmail());
		String originalView = (String) request.getSession().getAttribute(
				AuthenticationFilter.ORIGINAL_VIEW_KEY);
		if (originalView != null) {
			request.getSession().removeAttribute(
					AuthenticationFilter.ORIGINAL_VIEW_KEY);
			if (originalView.equals("/login.jsp")) {
				originalView = "/cms/index.jsp";
			}
		}
		else {
			originalView = "/cms/index.jsp";
		}
		return ServiceResponse.createSuccessResponse(originalView);
	}

	@Override
	public ServiceResponse logout(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		session.setAttribute(AuthenticationFilter.USER_SESSION_ATTR, null);
		return ServiceResponse.createSuccessResponse(Messages.get(
				"success_logout"));
	}
	

}
