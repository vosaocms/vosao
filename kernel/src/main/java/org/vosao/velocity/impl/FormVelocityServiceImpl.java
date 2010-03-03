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

package org.vosao.velocity.impl;

import java.io.StringWriter;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.vosao.business.Business;
import org.vosao.business.PageBusiness;
import org.vosao.dao.Dao;
import org.vosao.entity.FieldEntity;
import org.vosao.entity.FormConfigEntity;
import org.vosao.entity.FormEntity;
import org.vosao.global.SystemService;
import org.vosao.velocity.FormVelocityService;

public class FormVelocityServiceImpl implements FormVelocityService {

	private static final Log logger = LogFactory.getLog(FormVelocityServiceImpl.class);
	
	private Business business;
	
	public FormVelocityServiceImpl(Business aBusiness) {
		business = aBusiness;
	}
	
	@Override
	public String render(String formName) {
		try {
			FormEntity form = getDao().getFormDao().getByName(formName);
			if (form == null) {
				return "Error! Form " + formName + " was not found.";
			}
			List<FieldEntity> fields = getDao().getFieldDao().getByForm(form);
			FormConfigEntity formConfig = getDao().getFormConfigDao().getConfig();
			VelocityContext context = getPageBusiness().createContext(
				getBusiness().getLanguage());
			context.put("formConfig", formConfig);
			context.put("form", form);
			context.put("fields", fields);
			if (StringUtils.isEmpty(formConfig.getFormTemplate())) {
				return "Error! Form template is empty.";
			}
			StringWriter wr = new StringWriter();
			String log = null;
			getSystemService().getVelocityEngine().evaluate(
					context, wr, log, formConfig.getFormTemplate());
			return wr.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	private Business getBusiness() {
		return business;
	}
	
	private PageBusiness getPageBusiness() {
		return getBusiness().getPageBusiness();
	}

	private Dao getDao() {
		return getBusiness().getDao();
	}
	
	private SystemService getSystemService() {
		return getBusiness().getSystemService();
	}
	
}
