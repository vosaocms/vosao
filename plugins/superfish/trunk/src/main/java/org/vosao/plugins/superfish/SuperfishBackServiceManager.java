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

package org.vosao.plugins.superfish;

import org.jabsorb.JSONRPCBridge;
import org.vosao.business.Business;
import org.vosao.service.plugin.AbstractServicePlugin;
import org.vosao.service.plugin.PluginServiceManager;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class SuperfishBackServiceManager extends AbstractServicePlugin 
		implements PluginServiceManager {

	private SuperfishBackService backService;
	
	public SuperfishBackServiceManager(Business aBusiness) {
		setBusiness(aBusiness);
	}
	
	@Override
	public void register(JSONRPCBridge bridge) {
		bridge.registerObject("superfishBackService", getSuperfishBackService());
	}

	@Override
	public void unregister(JSONRPCBridge bridge) {
		bridge.unregisterObject("superfishBackService");
	}

	public SuperfishBackService getSuperfishBackService() {
		if (backService == null) {
			backService = new SuperfishBackService(getBusiness());
		}
		return backService;
	}

}
