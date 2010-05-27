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

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.vosao.business.Business;
import org.vosao.business.PageBusiness;
import org.vosao.common.Messages;
import org.vosao.dao.Dao;
import org.vosao.entity.FieldEntity;
import org.vosao.entity.FormConfigEntity;
import org.vosao.entity.FormDataEntity;
import org.vosao.entity.FormEntity;
import org.vosao.entity.helper.EntityHelper;
import org.vosao.global.SystemService;
import org.vosao.velocity.FormVelocityService;

/**
 * 
 * @author Alexander Oleynik
 *
 */
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
				return Messages.get("form.not_found", formName);
			}
			List<FieldEntity> fields = getDao().getFieldDao().getByForm(form);
			FormConfigEntity formConfig = getDao().getFormConfigDao().getConfig();
			VelocityContext context = getPageBusiness().createContext(
				getBusiness().getLanguage());
			context.put("formConfig", formConfig);
			context.put("form", form);
			context.put("fields", fields);
			if (StringUtils.isEmpty(formConfig.getFormTemplate())) {
				return Messages.get("form.template_is_empty");
			}
			return getSystemService().render(formConfig.getFormTemplate(), 
					context);
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

	@Override
	public List<FormDataEntity> findData(String formName) {
		FormEntity form = getDao().getFormDao().getByName(formName);
		if (formName != null) {
			List<FormDataEntity> result = getDao().getFormDataDao().getByForm(
					form);
			Collections.sort(result, EntityHelper.MOD_DATE_DESC);
			return result;
		}
		return Collections.EMPTY_LIST;
	}
	
}
