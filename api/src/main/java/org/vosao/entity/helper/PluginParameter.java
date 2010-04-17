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

package org.vosao.entity.helper;

import java.util.Date;

import org.dom4j.Element;
import org.vosao.enums.PluginConfigParameterType;
import org.vosao.utils.ParamUtil;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class PluginParameter {

	private String name;
	private String title;
	private PluginConfigParameterType type;
	private String value;
	private String defaultValue;

	public PluginParameter(Element element) {
		name = element.attributeValue("name");
		title = element.attributeValue("title");
		type = PluginConfigParameterType.valueOf(element.attributeValue("type")
				.toUpperCase());
		defaultValue = element.attributeValue("title");
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public PluginConfigParameterType getType() {
		return type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}
	
	public Integer getDefaultValueInteger() {
		return ParamUtil.getInteger(defaultValue, 0);
	}

	public Boolean getDefaultValueBoolean() {
		return ParamUtil.getBoolean(defaultValue, false);
	}

	public Date getDefaultValueDate() {
		return ParamUtil.getDate(defaultValue, new Date());
	}

	public Integer getValueInteger() {
		return ParamUtil.getInteger(value, getDefaultValueInteger());
	}

	public Boolean getValueBoolean() {
		return ParamUtil.getBoolean(value, getDefaultValueBoolean());
	}

	public Date getValueDate() {
		return ParamUtil.getDate(value, getDefaultValueDate());
	}

}
