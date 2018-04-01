/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.business.impl.imex;

import static org.vosao.utils.XmlUtil.notNull;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.imex.ConfigExporter;
import org.vosao.common.VosaoContext;
import org.vosao.dao.DaoTaskException;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.LanguageEntity;
import org.vosao.utils.XmlUtil;

public class ConfigExporterImpl extends AbstractExporter 
		implements ConfigExporter {

	public ConfigExporterImpl(ExporterFactoryImpl factory) {
		super(factory);
	}
	
	@Override
	public String createConfigXML() {
		Document doc = DocumentHelper.createDocument();
		Element configElement = doc.addElement("config");
		createConfigXML(configElement);
		return doc.asXML();
	}		
	
	private void createConfigXML(Element configElement) {
		ConfigEntity config = getConfig();
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
		configElement.addElement("enableCkeditor").setText(
				String.valueOf(config.isEnableCkeditor()));
		configElement.addElement("attributes").setText(notNull(
				config.getAttributesJSON()));
		configElement.addElement("defaultTimezone").setText(notNull(
				config.getDefaultTimezone()));
		configElement.addElement("defaultLanguage").setText(notNull(
				config.getDefaultLanguage()));
		configElement.addElement("site404Url").setText(notNull(
				config.getSite404Url()));
		createLanguagesXML(configElement);
	}

	private void createLanguagesXML(Element configElement) {
		Element languagesElement = configElement.addElement("languages");
		List<LanguageEntity> langs = getDao().getLanguageDao().select();
		for (LanguageEntity lang : langs) {
			Element langElem = languagesElement.addElement("language");
			langElem.addAttribute("code", lang.getCode());
			langElem.addAttribute("title", lang.getTitle());
		}
	}
	
	public void readConfigs(Element configElement) throws DaoTaskException {
		ConfigEntity config = getConfig();
		for (Iterator<Element> i = configElement.elementIterator(); i.hasNext(); ) {
            Element element = i.next();
            if (element.getName().equals("google-analytics")) {
            	config.setGoogleAnalyticsId(element.getText());
            }
            if (element.getName().equals("email")) {
            	config.setSiteEmail(element.getText());
            }
            if (element.getName().equals("domain")) {
            	config.setSiteDomain(element.getText());
            }
            if (element.getName().equals("edit-ext")) {
            	config.setEditExt(element.getText());
            }
            if (element.getName().equals("recaptchaPrivateKey")) {
            	config.setRecaptchaPrivateKey(element.getText());
            }
            if (element.getName().equals("recaptchaPublicKey")) {
            	config.setRecaptchaPublicKey(element.getText());
            }
            if (element.getName().equals("commentsEmail")) {
            	config.setCommentsEmail(element.getText());
            }
            if (element.getName().equals("commentsTemplate")) {
            	config.setCommentsTemplate(element.getText());
            }
            if (element.getName().equals("languages")) {
            	readLanguages(element);
            }
            if (element.getName().equals("attributes")) {
            	config.setAttributesJSON(element.getText());
            }
            if (element.getName().equals("enableRecaptcha")) {
            	config.setEnableRecaptcha(XmlUtil.readBooleanText(
            			element, false));
            }
            if (element.getName().equals("siteUserLoginUrl")) {
            	config.setSiteUserLoginUrl(element.getText());
            }
            if (element.getName().equals("enableCkeditor")) {
            	config.setEnableCkeditor(XmlUtil.readBooleanText(
            			element, false));
            }
            if (element.getName().equals("defaultTimezone")) {
            	config.setDefaultTimezone(element.getText());
            }
            if (element.getName().equals("defaultLanguage")) {
            	config.setDefaultLanguage(element.getText());
            }
            if (element.getName().equals("site404Url")) {
            	config.setSite404Url(element.getText());
            }
		}
		getDaoTaskAdapter().configSave(config);
	}
	
	public void readLanguages(Element languagesElement) throws DaoTaskException {
		for (Iterator<Element> i = languagesElement.elementIterator(); i.hasNext(); ) {
            Element element = i.next();
            if (element.getName().equals("language")) {
            	String code = element.attributeValue("code");
            	String title = element.attributeValue("title");
            	LanguageEntity language = getDao().getLanguageDao().getByCode(
            			code);
            	if (language == null) {
                	language = new LanguageEntity(code, title);
            	}
            	else {
            		language.setTitle(title);
            	}
            	getDaoTaskAdapter().languageSave(language);
            }
		}
	}
	
	private ConfigEntity getConfig() {
		return VosaoContext.getInstance().getConfig();
	}
	
	/**
	 * Parse and import data from _config.xml file.
	 * @param xml - _config.xml content.
	 * @throws DocumentException 
	 * @throws DaoTaskException 
	 */
	public void readConfigFile(String xml) throws DocumentException, 
			DaoTaskException {
		Document doc = DocumentHelper.parseText(xml);
		readConfigs(doc.getRootElement());
	}
	
}
