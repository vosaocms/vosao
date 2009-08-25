package org.vosao.jsf;
import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vosao.entity.UserEntity;
import org.vosao.filter.AuthenticationFilter;


public class LoginBean extends AbstractJSFBean implements Serializable {

	private static final long serialVersionUID = 2L;
	
	private String email;
	private String password;
	
	public void login() throws IOException {
		UserEntity user = getDao().getUserDao().getByEmail(email);
		if (user == null) {
			JSFUtil.addErrorMessage("User was not found.");
			return;
		}
		if (!user.getPassword().equals(password)) {
			JSFUtil.addErrorMessage("Password incorrect.");
			return;
		}
		getBusiness().getUserPreferences().setUser(user);
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		String originalView = (String) request.getSession().getAttribute(
				AuthenticationFilter.ORIGINAL_VIEW_KEY);
		request.getSession().removeAttribute(
				AuthenticationFilter.ORIGINAL_VIEW_KEY);
		response.sendRedirect(originalView);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
