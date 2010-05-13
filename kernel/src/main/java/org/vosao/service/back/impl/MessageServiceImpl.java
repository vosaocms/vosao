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

package org.vosao.service.back.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.vosao.common.Messages;
import org.vosao.entity.LanguageEntity;
import org.vosao.entity.MessageEntity;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.MessageService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.service.vo.MessageVO;

/**
 * @author Alexander Oleynik
 */
public class MessageServiceImpl extends AbstractServiceImpl 
		implements MessageService {

	@Override
	public List<MessageVO> select() {
		List<MessageVO> result = new ArrayList<MessageVO>();
		Map<String, Map<String, String>> messagesMap = 
				new HashMap<String, Map<String,String>>();
		List<MessageEntity> messages = getDao().getMessageDao().select();
		for (MessageEntity message : messages) {
			Map<String, String> langMap = messagesMap.get(message.getCode());
			if (langMap == null) {
				langMap = new HashMap<String, String>();
				messagesMap.put(message.getCode(), langMap);
			}
			langMap.put(message.getLanguageCode(), message.getValue());
		}
		List<String> keys = new ArrayList<String>();
		keys.addAll(messagesMap.keySet());
		Collections.sort(keys);
		for (String code : keys) {
			result.add(new MessageVO(code, messagesMap.get(code)));
		}
		return result;
	}

	@Override
	public ServiceResponse remove(List<String> codes) {
		List<Long> ids = new ArrayList<Long>();
		for (String code : codes) {
			List<MessageEntity> messages = getDao().getMessageDao()
					.selectByCode(code);
			for (MessageEntity message : messages) {
				ids.add(message.getId());
			}
		}
		getDao().getMessageDao().remove(ids);
		return ServiceResponse.createSuccessResponse(
				Messages.get("messages.success_deleted"));
	}

	@Override
	public MessageEntity getById(Long id) {
		return getDao().getMessageDao().getById(id);
	}

	@Override
	public ServiceResponse save(Map<String, String> vo) {
		List<String> errors = new ArrayList<String>();
		if (StringUtils.isEmpty(vo.get("code"))) {
			errors.add(Messages.get("code_is_empty"));
		}
		else {
			String code = StringUtils.strip(vo.get("code"));
			List<LanguageEntity> languages = getDao().getLanguageDao().select();
			for (LanguageEntity lang : languages) {
				if (!StringUtils.isEmpty(vo.get(lang.getCode()))) {
					MessageEntity message = getDao().getMessageDao().getByCode(
							code, lang.getCode());
					if (message == null) {
						message = new MessageEntity();
						message.setCode(code);
						message.setLanguageCode(lang.getCode());
					}
					message.setValue(vo.get(lang.getCode()));
					getDao().getMessageDao().save(message);
				}
			}
			return ServiceResponse.createSuccessResponse(
					Messages.get("message.success_save"));
		}
		return ServiceResponse.createErrorResponse(
				Messages.get("errors_occured"), errors);
	}

	@Override
	public List<MessageEntity> selectByCode(String code) {
		return getDao().getMessageDao().selectByCode(code);
	}

}
