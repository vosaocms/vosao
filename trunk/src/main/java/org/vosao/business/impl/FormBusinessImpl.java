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

package org.vosao.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.FormBusiness;
import org.vosao.entity.FormEntity;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

public class FormBusinessImpl extends AbstractBusinessImpl 
	implements FormBusiness {

	private static final Log logger = LogFactory.getLog(FormBusinessImpl.class);
	
	@Override
	public List<String> validateBeforeUpdate(final FormEntity entity) {
		List<String> errors = new ArrayList<String>();
		if (entity.getId() == null) {
			FormEntity myForm = getDao().getFormDao().getByName(entity.getName());
			if (myForm != null) {
				errors.add("Form with such name already exists");
			}
		}
		if (StringUtil.isEmpty(entity.getName())) {
			errors.add("Name is empty");
		}
		if (StringUtil.isEmpty(entity.getTitle())) {
			errors.add("Title is empty");
		}
		if (StringUtil.isEmpty(entity.getEmail())) {
			errors.add("Email is empty");
		}
		return errors;
	}
	
}
