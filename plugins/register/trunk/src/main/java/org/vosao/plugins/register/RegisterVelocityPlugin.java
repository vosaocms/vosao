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

package org.vosao.plugins.register;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.vosao.business.Business;
import org.vosao.entity.ConfigEntity;
import org.vosao.plugins.register.dao.RegisterDao;
import org.vosao.plugins.register.entity.RegisterConfigEntity;
import org.vosao.velocity.plugin.AbstractVelocityPlugin;

public class RegisterVelocityPlugin extends AbstractVelocityPlugin {

		private static final Log logger = LogFactory.getLog(
				RegisterVelocityPlugin.class);

		private RegisterDao registerDao;
		
		public RegisterVelocityPlugin(Business business, RegisterDao aRegisterDao) {
			setBusiness(business);
			registerDao = aRegisterDao;
		}
		
		public String renderForm() {
			RegisterConfigEntity registerConfig = registerDao.getRegisterConfigDao()
					.getConfig();
			ConfigEntity config = getDao().getConfigDao().getConfig();
			VelocityContext context = new VelocityContext();
			context.put("registerConfig", registerConfig);
			context.put("config", config);
			return getBusiness().getSystemService().render(
					registerConfig.getRegisterFormTemplate(), context);
		}
}
