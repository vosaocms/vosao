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

package org.vosao.plugins.register;

import org.vosao.business.plugin.AbstractPluginEntryPoint;
import org.vosao.plugins.register.cronjob.CleanupConfirmationsJob;
import org.vosao.plugins.register.dao.RegisterDao;
import org.vosao.plugins.register.service.RegisterBackServiceManager;
import org.vosao.plugins.register.service.RegisterFrontServiceManager;
import org.vosao.plugins.register.servlet.ConfirmServlet;
import org.vosao.service.plugin.PluginServiceManager;

public class RegisterEntryPoint extends AbstractPluginEntryPoint {

	private RegisterDao registerDao;
	private RegisterBackServiceManager registerBackSeriviceManager;
	private RegisterFrontServiceManager registerFrontSeriviceManager;
	private RegisterVelocityPlugin velocityPlugin;
	
	@Override
	public void init() {
		getServlets().put("confirm", new ConfirmServlet(
				getRegisterDao(),
				getRegisterBackServiceManager().getRegisterBackService()));
		getJobs().add(new CleanupConfirmationsJob(
				getRegisterDao()));
	}
	
	@Override
	public PluginServiceManager getPluginBackService() {
		return getRegisterBackServiceManager();
	}

	private RegisterBackServiceManager getRegisterBackServiceManager() {
		if (registerBackSeriviceManager == null) {
			registerBackSeriviceManager = new RegisterBackServiceManager(getBusiness(),
					getRegisterDao());
		}
		return registerBackSeriviceManager;
	}

	@Override
	public PluginServiceManager getPluginFrontService() {
		return getRegisterFrontServiceManager();
	}

	private RegisterFrontServiceManager getRegisterFrontServiceManager() {
		if (registerFrontSeriviceManager == null) {
			registerFrontSeriviceManager = new RegisterFrontServiceManager(getBusiness(),
					getRegisterDao());
		}
		return registerFrontSeriviceManager;
	}

	private RegisterDao getRegisterDao() {
		if (registerDao == null) {
			registerDao = new RegisterDao(getBusiness());
		}
		return registerDao;
	}
	
	@Override
	public Object getPluginVelocityService() {
		if (velocityPlugin == null) {
			velocityPlugin = new RegisterVelocityPlugin(getBusiness(), 
					getRegisterDao());
		}
		return velocityPlugin;
	}
	
}
