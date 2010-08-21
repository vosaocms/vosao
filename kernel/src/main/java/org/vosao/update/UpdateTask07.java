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

package org.vosao.update;

import org.vosao.business.Business;
import org.vosao.business.impl.mq.AbstractSubscriber;
import org.vosao.business.mq.Message;
import org.vosao.business.mq.Topic;
import org.vosao.business.mq.message.SimpleMessage;
import org.vosao.dao.Dao;
import org.vosao.entity.PageEntity;
import org.vosao.entity.PluginEntity;
import org.vosao.entity.StructureTemplateEntity;
import org.vosao.entity.UserEntity;
import org.vosao.update.verion_0_7.PageTitleUpdate;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class UpdateTask07 implements UpdateTask {

	private Business business;
	
	public UpdateTask07(Business aBusiness) {
		business = aBusiness;
	}
	
	private Dao getDao() {
		return business.getDao();
	}
	
	private Business getBusiness() {
		return business;
	}

	@Override
	public String getFromVersion() {
		return "0.6";
	}

	@Override
	public String getToVersion() {
		return "0.7";
	}

	@Override
	public String update() throws UpdateException {
		updatePlugins();
		updatePages();
		return "Successfully updated to 0.7 version.";
	}

	private void updatePlugins() {
		for (PluginEntity plugin : getDao().getPluginDao().select()) {
			plugin.setDisabled(true);
			getDao().getPluginDao().save(plugin);
		}
	}

	private void updatePages() {
		Message msg = new SimpleMessage(PageTitleUpdate.class);
		getBusiness().getMessageQueue().publish(msg);
	}
	
}
