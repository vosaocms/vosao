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

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.Business;
import org.vosao.entity.FieldEntity;
import org.vosao.entity.FormConfigEntity;
import org.vosao.entity.FormEntity;
import org.vosao.webdav.AbstractFileResource;

import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;

public class FormsFileResource extends AbstractFileResource {

	public FormsFileResource(Business aBusiness, String name) {
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
		Element e = doc.addElement("forms");
		createFormConfigXML(e);
		List<FormEntity> list = getDao().getFormDao().select();
		for (FormEntity form : list) {
			createFormXML(e, form);
		}
		setData(doc.asXML().getBytes("UTF-8"));
	}
	
	private void createFormConfigXML(Element formsElement) {
		FormConfigEntity config = getDao().getFormConfigDao().getConfig();
		Element configElement = formsElement.addElement("form-config");
		Element formTemplateElement = configElement.addElement("formTemplate");
		formTemplateElement.setText(config.getFormTemplate());
		Element formLetterElement = configElement.addElement("letterTemplate");
		formLetterElement.setText(config.getLetterTemplate());
	}

	private void createFormXML(Element formsElement, final FormEntity form) {
		Element formElement = formsElement.addElement("form");
		formElement.addAttribute("name", form.getName());
		formElement.addAttribute("title", form.getTitle());
		formElement.addAttribute("email", form.getEmail());
		formElement.addAttribute("letterSubject", form.getLetterSubject());
		formElement.addAttribute("sendButtonTitle", form.getSendButtonTitle());
		formElement.addAttribute("showResetButton", String.valueOf(
				form.isShowResetButton()));
		formElement.addAttribute("enableCaptcha", String.valueOf(
				form.isEnableCaptcha()));
		formElement.addAttribute("resetButtonTitle", form.getResetButtonTitle());
		List<FieldEntity> fields = getDao().getFieldDao().getByForm(form);
		for (FieldEntity field : fields) {
			createFieldXML(formElement, field);
		}
	}
	
	private void createFieldXML(Element formElement, FieldEntity field) {
		Element fieldElement = formElement.addElement("field");
		fieldElement.addAttribute("name", field.getName());
		fieldElement.addAttribute("title", field.getTitle());
		fieldElement.addAttribute("fieldType", field.getFieldType().name());
		fieldElement.addAttribute("mandatory", String.valueOf(field.isMandatory()));
		fieldElement.addAttribute("values", field.getValues());
		fieldElement.addAttribute("defaultValue", field.getDefaultValue());
		fieldElement.addAttribute("height", String.valueOf(field.getHeight()));
		fieldElement.addAttribute("width", String.valueOf(field.getWidth()));
		fieldElement.addAttribute("index", String.valueOf(field.getIndex()));
	}

}
