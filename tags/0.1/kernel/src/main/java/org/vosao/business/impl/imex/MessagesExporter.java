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

package org.vosao.business.impl.imex;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.entity.MessageEntity;
import org.vosao.entity.UserEntity;
import org.vosao.entity.helper.UserHelper;

/**
 * @author Alexander Oleynik
 */
public class MessagesExporter extends AbstractExporter {

	public MessagesExporter(Dao aDao, Business aBusiness) {
		super(aDao, aBusiness);
	}
	
	public void createMessagesXML(Element siteElement) {
		Element messagesElement = siteElement.addElement("messages");
		List<MessageEntity> list = getDao().getMessageDao().select();
		for (MessageEntity message : list) {
			createMessageXML(messagesElement, message);
		}
	}

	private void createMessageXML(Element messagesElement, 
			final MessageEntity message) {
		Element messageElement = messagesElement.addElement("message");
		messageElement.addElement("language").setText(
				message.getLanguageCode());
		messageElement.addElement("code").setText(message.getCode());
		messageElement.addElement("value").setText(message.getValue());
	}
	
	public void readMessages(Element messagesElement) {
		for (Iterator<Element> i = messagesElement.elementIterator(); 
				i.hasNext(); ) {
            Element element = i.next();
            if (element.getName().equals("message")) {
            	String language = element.elementText("language");
            	String code = element.elementText("code");
            	String value = element.elementText("value");
            	MessageEntity message = getDao().getMessageDao().getByCode(
            			code, language);
            	if (message == null) {
            		message = new MessageEntity(code, language, value);
            	}
            	message.setValue(value);
            	getDao().getMessageDao().save(message);
            }
		}		
	}
}
