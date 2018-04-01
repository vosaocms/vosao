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

package org.vosao.service.back;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.vosao.entity.FormConfigEntity;
import org.vosao.entity.FormDataEntity;
import org.vosao.entity.FormEntity;
import org.vosao.service.AbstractService;
import org.vosao.service.ServiceResponse;


public interface FormService extends AbstractService {
	
	ServiceResponse saveForm(Map<String, String> vo);
	
	FormEntity getForm(final Long formId);
	
	List<FormEntity> select();
	
	ServiceResponse deleteForm(final List<String> ids);
	
	FormConfigEntity getFormConfig();
	
	ServiceResponse saveFormConfig(final Map<String, String> vo);

	ServiceResponse restoreFormTemplate() throws IOException;
	
	ServiceResponse restoreFormLetter() throws IOException;
	
	ServiceResponse removeData(List<String> ids);
	
	List<FormDataEntity> getFormData(Long formId);

	ServiceResponse sendFormLetter(Long formDataId);

}
