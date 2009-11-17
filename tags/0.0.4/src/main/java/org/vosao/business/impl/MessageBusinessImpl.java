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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datanucleus.util.StringUtils;
import org.vosao.business.MessageBusiness;
import org.vosao.entity.MessageEntity;

/**
 * @author Alexander Oleynik
 */
public class MessageBusinessImpl extends AbstractBusinessImpl 
	implements MessageBusiness {

	private static final Log logger = LogFactory.getLog(MessageBusinessImpl.class);

	private String getBundleKey(final String languageCode) {
		return "messages_" + languageCode;	
	}
	
	@Override
	public Map<String, String> getBundle(String languageCode) {
		Map<String, String> result = (Map<String, String>) getSystemService()
				.getCache().get(getBundleKey(languageCode));
		if (result == null) {
			List<MessageEntity> messages = getDao().getMessageDao().select(
					languageCode);
			result = new HashMap<String, String>();
			for (MessageEntity message : messages) {
				result.put(message.getCode(), message.getValue());
			}
		}
		return result;
	}

	@Override
	public void resetBundleCache(String languageCode) {
		getSystemService().getCache().remove(getBundleKey(languageCode));
	}

	@Override
	public List<String> validateBeforeUpdate(MessageEntity entity) {
		List<String> errors = new ArrayList<String>();
		if (StringUtils.isEmpty(entity.getCode())) {
			errors.add("Code is empty");
		}
		if (StringUtils.isEmpty(entity.getLanguageCode())) {
			errors.add("Language code is empty");
		}
		return errors;
	}
	
}
