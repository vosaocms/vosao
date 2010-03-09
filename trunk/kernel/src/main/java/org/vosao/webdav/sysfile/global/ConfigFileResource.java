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

package org.vosao.webdav.sysfile.global;

import static org.vosao.utils.XmlUtil.notNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.Business;
import org.vosao.entity.ConfigEntity;
import org.vosao.webdav.AbstractFileResource;

import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;

public class ConfigFileResource extends AbstractFileResource {

	public ConfigFileResource(Business aBusiness, String name) {
		super(aBusiness, name, new Date());
		setContentType("text/xml");
		setData(new byte[0]);
	}

	@Override
	public void sendContent(OutputStream out, Range range,
			Map<String, String> params, String aContentType) throws IOException,
			NotAuthorizedException, BadRequestException {
		createXML();
		super.sendContent(out, range, params, aContentType);
	}

	private void createXML() throws UnsupportedEncodingException {
		Document doc = DocumentHelper.createDocument();
		Element configElement = doc.addElement("config");
		ConfigEntity config = getBusiness().getConfigBusiness().getConfig();
		configElement.addElement("google-analytics").setText(notNull(
				config.getGoogleAnalyticsId()));
		configElement.addElement("email").setText(notNull(
				config.getSiteEmail()));
		configElement.addElement("domain").setText(notNull(
				config.getSiteDomain()));
		configElement.addElement("edit-ext").setText(notNull(
				config.getEditExt()));
		configElement.addElement("recaptchaPrivateKey").setText(notNull(
				config.getRecaptchaPrivateKey()));
		configElement.addElement("recaptchaPublicKey").setText(notNull(
				config.getRecaptchaPublicKey()));
		configElement.addElement("commentsEmail").setText(notNull(
				config.getCommentsEmail()));
		configElement.addElement("commentsTemplate").setText(notNull(
				config.getCommentsTemplate()));
		configElement.addElement("enableRecaptcha").setText(
			String.valueOf(config.isEnableRecaptcha()));
		configElement.addElement("version").setText(notNull(
				config.getVersion()));
		configElement.addElement("siteUserLoginUrl").setText(notNull(
				config.getSiteUserLoginUrl()));
		setData(doc.asXML().getBytes("UTF-8"));
	}
	
}
