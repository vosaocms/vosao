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

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.vosao.dao.Dao;
import org.vosao.entity.FieldEntity;
import org.vosao.entity.FormConfigEntity;
import org.vosao.entity.FormEntity;
import org.vosao.global.SystemService;
import org.vosao.velocity.FormVelocityService;

public class FormVelocityServiceImpl implements FormVelocityService {

	private Dao dao;
	private SystemService systemService;
	
	public FormVelocityServiceImpl(Dao aDao, SystemService aSystemService) {
		setDao(aDao);
		setSystemService(aSystemService);
	}
	
	private Dao getDao() {
		return dao;
	}
	
	private void setDao(Dao aDao) {
		dao = aDao;
	}

	@Override
	public String render(String formName) {
		FormEntity form = getDao().getFormDao().getByName(formName);
		if (form == null) {
			return "Error! Form " + formName + " was not found.";
		}
		List<FieldEntity> fields = getDao().getFieldDao().getByForm(form);
		FormConfigEntity formConfig = getDao().getFormConfigDao().getConfig();
		VelocityContext context = new VelocityContext();
		context.put("config", getDao().getConfigDao().getConfig());
		context.put("formConfig", formConfig);
		context.put("form", form);
		context.put("fields", fields);
		if (StringUtils.isEmpty(formConfig.getFormTemplate())) {
			return "Error! Form template is empty.";
		}
		StringWriter wr = new StringWriter();
		String log = null;
		try {
			getSystemService().getVelocityEngine().evaluate(
					context, wr, log, formConfig.getFormTemplate());
			return wr.toString();
		} catch (ParseErrorException e) {
			return e.toString();
		} catch (MethodInvocationException e) {
			return e.toString();
		} catch (ResourceNotFoundException e) {
			return e.toString();
		} catch (IOException e) {
			return e.toString();
		}
	}

	public SystemService getSystemService() {
		return systemService;
	}

	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

}
