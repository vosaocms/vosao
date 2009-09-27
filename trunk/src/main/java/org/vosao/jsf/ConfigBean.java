/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * Copyright (C) 2009 Vosao development team
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.jsf;
import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.servlet.MimeType;


public class ConfigBean extends AbstractJSFBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(ConfigBean.class);
	
	private String googleAnalyticsId;
	private String siteEmail;
	private String siteDomain;
	private String editExt;
	private String recaptchaPrivateKey;
	private String recaptchaPublicKey;
	
	public void init() {
		googleAnalyticsId = getBusiness().getConfigBusiness()
				.getGoogleAnalyticsId();
		siteEmail = getBusiness().getConfigBusiness().getSiteEmail();
		siteDomain = getBusiness().getConfigBusiness().getSiteDomain();
		editExt = getBusiness().getConfigBusiness().getEditExt();
		recaptchaPrivateKey = getBusiness().getConfigBusiness()
			.getRecaptchaPrivateKey();
		recaptchaPublicKey = getBusiness().getConfigBusiness()
			.getRecaptchaPublicKey();
	}
	
	public void export() throws IOException {
		log.debug("Exporting site.");
		HttpServletResponse response = JSFUtil.getResponse();
		response.setContentType(MimeType.getContentTypeByExt("zip"));
		String downloadFile = "exportSite.zip";
		response.addHeader("Content-Disposition", "attachment; filename=\"" 
				+ downloadFile + "\"");
		ServletOutputStream out = response.getOutputStream();
		byte[] file = getBusiness().getImportExportBusiness().createExportFile();
		response.setContentLength(file.length);
		out.write(file);
		out.flush();
		out.close();
		FacesContext.getCurrentInstance().responseComplete();
	}
	
	public void save() {
		getBusiness().getConfigBusiness().setGoogleAnalyticsId(
				googleAnalyticsId);
		getBusiness().getConfigBusiness().setSiteEmail(siteEmail);
		getBusiness().getConfigBusiness().setSiteDomain(siteDomain);
		getBusiness().getConfigBusiness().setEditExt(editExt);
		getBusiness().getConfigBusiness().setRecaptchaPrivateKey(
				recaptchaPrivateKey);
		getBusiness().getConfigBusiness().setRecaptchaPublicKey(
				recaptchaPublicKey);
		JSFUtil.addInfoMessage("Configuration was successfully saved.");
	}

	public String getGoogleAnalyticsId() {
		return googleAnalyticsId;
	}

	public void setGoogleAnalyticsId(String googleAnalyticsId) {
		this.googleAnalyticsId = googleAnalyticsId;
	}

	public String getSiteEmail() {
		return siteEmail;
	}

	public void setSiteEmail(String siteEmail) {
		this.siteEmail = siteEmail;
	}

	public String getSiteDomain() {
		return siteDomain;
	}

	public void setSiteDomain(String siteDomain) {
		this.siteDomain = siteDomain;
	}

	public String getEditExt() {
		return editExt;
	}

	public void setEditExt(String editExt) {
		this.editExt = editExt;
	}
	
	public String getRecaptchaPrivateKey() {
		return recaptchaPrivateKey;
	}

	public void setRecaptchaPrivateKey(String recaptcha) {
		this.recaptchaPrivateKey = recaptcha;
	}

	public String getRecaptchaPublicKey() {
		return recaptchaPublicKey;
	}

	public void setRecaptchaPublicKey(String recaptchaPublicKey) {
		this.recaptchaPublicKey = recaptchaPublicKey;
	}
}
