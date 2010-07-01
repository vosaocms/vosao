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

package org.vosao.dao;

import java.util.List;

import org.vosao.entity.LanguageEntity;
import org.vosao.entity.MessageEntity;


public class MessageDaoTest extends AbstractDaoTest {

	private MessageEntity addMessage(String code, String languageCode,
			String value) {
		return getDao().getMessageDao().save(new MessageEntity(code, 
				languageCode, value));
	}
	
	private void init() {
		addMessage("test", "en", "test_en");
		addMessage("test", "ru", "test_ru");
		addMessage("test", "uk", "test_uk");
		addMessage("test2", "en", "test2_en");
		addMessage("test2", "ru", "test2_ru");
		addMessage("test2", "uk", "test2_uk");
	}
	
	public void testSelectByCode() {
		init();
		List<MessageEntity> list = getDao().getMessageDao().selectByCode("test");
		assertEquals(3, list.size());
		list = getDao().getMessageDao().selectByCode(null);
		assertEquals(0, list.size());
		list = getDao().getMessageDao().selectByCode("null");
		assertEquals(0, list.size());
	}
	
	public void testGetByCode() {
		init();
		MessageEntity m = getDao().getMessageDao().getByCode("test", "en");
		assertNotNull(m);
		assertEquals("test_en", m.getValue());
		m = getDao().getMessageDao().getByCode("test3", "en");
		assertNull(m);
		m = getDao().getMessageDao().getByCode(null, null);
		assertNull(m);
		m = getDao().getMessageDao().getByCode("test", null);
		assertNull(m);
		m = getDao().getMessageDao().getByCode(null, "en");
		assertNull(m);
	}

	public void testSelect() {
		init();
		List<MessageEntity> list = getDao().getMessageDao().select("en");
		assertEquals(2, list.size());
		list = getDao().getMessageDao().select("ru");
		assertEquals(2, list.size());
		String lang = null;
		list = getDao().getMessageDao().select(lang);
		assertEquals(0, list.size());
		list = getDao().getMessageDao().select("fr");
		assertEquals(0, list.size());
	}
	
}
