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

package org.vosao.business;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.vosao.business.vo.PluginPropertyVO;
import org.vosao.common.PluginException;
import org.vosao.entity.PluginEntity;
import org.vosao.velocity.plugin.VelocityPlugin;

/**
 * @author Alexander Oleynik
 */
public interface PluginBusiness {

	/**
	 * Installs war file as Vosao plugin.
	 * @param filename
	 * @param data
	 * @throws IOException 
	 * @throws PluginException 
	 * @throws DocumentException 
	 */
	void install(String filename, byte[] data) 
			throws IOException, PluginException, DocumentException;
	
	void uninstall(PluginEntity plugin);
	
	VelocityPlugin getVelocityPlugin(PluginEntity plugin) 
			throws ClassNotFoundException, InstantiationException, 
			IllegalAccessException;
	
	void resetPlugin(PluginEntity plugin);
	
	List<PluginPropertyVO> getProperties(PluginEntity plugin);

	Map<String, PluginPropertyVO> getPropertiesMap(PluginEntity plugin);
	
}