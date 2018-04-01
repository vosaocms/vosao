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

package org.vosao.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.vosao.business.MessageBusiness;
import org.vosao.entity.MessageEntity;
import org.vosao.i18n.Messages;

/**
 * @author Alexander Oleynik
 */
public class MessageBusinessImpl extends AbstractBusinessImpl 
	implements MessageBusiness {

	private static final String DEFAULT_BUNDLE_LANGUAGE = "en";
	
	@Override
	public Map<String, String> getBundle(String languageCode) {
		Map<String, String> result = new HashMap<String, String>();
		addMessages(result, DEFAULT_BUNDLE_LANGUAGE);
		addMessages(result, languageCode);
		return result;
	}

	private void addMessages(Map<String, String> bundle, String language) {
		List<MessageEntity> messages = getDao().getMessageDao().select(
				language);
		for (MessageEntity message : messages) {
			if (!StringUtils.isEmpty(message.getValue())) {
				bundle.put(message.getCode(), message.getValue());
			}
		}
	}
	
	@Override
	public List<String> validateBeforeUpdate(MessageEntity entity) {
		List<String> errors = new ArrayList<String>();
		if (StringUtils.isEmpty(entity.getCode())) {
			errors.add(Messages.get("code_is_empty"));
		}
		if (StringUtils.isEmpty(entity.getLanguageCode())) {
			errors.add(Messages.get("language_code_is_empty"));
		}
		return errors;
	}
	
}
