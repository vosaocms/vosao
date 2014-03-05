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

package org.vosao.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vosao.command.CommandMessage;
import org.vosao.command.PageSearchCommand;
import org.vosao.common.VosaoContext;

/**
 * Servlet for starting command tasks linked to channel API.
 * 
 * @author Aleksandr Oleynik 
 */
public class ChannelCommandServlet extends AbstractServlet {
	
	private static final long serialVersionUID = 1L;

	// Mapping between string command and task class
	private static final Object[] COMMANDS = {
		"pageSearch", PageSearchCommand.class
	};
	
	private static Map<String, String> getParameters(HttpServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		for (Object key : request.getParameterMap().keySet()) {
			if (!("clientId".equals(key) || "cmd".equals(key))) {
				result.put((String)key, 
						((String[])request.getParameterMap().get(key))[0]);
			}
		}
		return result;
	}
	
	private Map<String, Class> commands = null;
	
	public  Class getCommand(String key) {
		if (commands == null) {
			 commands = new HashMap<String, Class>();
			 for (int i=0; i < COMMANDS.length; i+=2) {
				 commands.put((String)COMMANDS[i], (Class)COMMANDS[i+1]);
			 }
		}
		return commands.get(key);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		String clientId = request.getParameter("clientId");
		Class commandClass = getCommand(cmd);
		if (commandClass != null) {
			CommandMessage msg = new CommandMessage(clientId, 
					commandClass.getName(), getParameters(request));
			
			VosaoContext.getInstance().getMessageQueue().publish(msg);
		}
	}
	
}