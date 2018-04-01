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

package org.vosao.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

public class EntityUtil {

	private static final Log logger = LogFactory.getLog(
			EntityUtil.class);

	public static String getKind(Class clazz) {
		String name = clazz.getName();
		return name.substring(name.lastIndexOf('.') + 1);
	}

	public static Integer getIntegerProperty(Entity entity, String name,
			int defaultValue) {
		Object p = entity.getProperty(name);
		if (p == null) {
			return defaultValue;
		}
		if (p instanceof Integer) {
			return (Integer) p;
		}
		if (p instanceof Long) {
			return ((Long) p).intValue();
		}
		return defaultValue;
	}

	public static Long getLongProperty(Entity entity, String name,
			Long defaultValue) {
		Object p = entity.getProperty(name);
		if (p == null) {
			return defaultValue;
		}
		if (p instanceof Long) {
			return (Long) p;
		}
		return defaultValue;
	}

	public static Long getLongProperty(Entity entity, String name) {
		return getLongProperty(entity, name, null);
	}

	public static String getStringProperty(Entity entity, String name) {
		return getStringProperty(entity, name, null);
	}

	public static String getStringProperty(Entity entity, String name,
			String defaultValue) {
		Object p = entity.getProperty(name);
		if (p == null) {
			return defaultValue;
		}
		if (p instanceof String) {
			return (String) p;
		}
		return defaultValue;
	}

	public static String getTextProperty(Entity entity, String name) {
		Object p = entity.getProperty(name);
		if (p == null) {
			return null;
		}
		if (p instanceof Text) {
			return ((Text) p).getValue();
		}
		return null;
	}

	public static Date getDateProperty(Entity entity, String name) {
		Object p = entity.getProperty(name);
		if (p == null) {
			return null;
		}
		if (p instanceof Date) {
			return (Date) p;
		}
		return null;
	}

	public static boolean getBooleanProperty(Entity entity, String name,
			boolean defaultValue) {
		Object p = entity.getProperty(name);
		if (p == null) {
			return defaultValue;
		}
		if (p instanceof Boolean) {
			return (Boolean) p;
		}
		return defaultValue;
	}

	public static byte[] getBlobProperty(Entity entity, String name) {
		Object p = entity.getProperty(name);
		if (p == null) {
			return null;
		}
		if (p instanceof Blob) {
			return ((Blob) p).getBytes();
		}
		return null;
	}

	public static List getListProperty(Entity entity, String name) {
		Object p = entity.getProperty(name);
		if (p == null) {
			return new ArrayList();
		}
		if (p instanceof List) {
			return (List) p;
		}
		return new ArrayList();
	}

	public static <K,T> Map<K,T> getMapProperty(Entity entity, String name) {
		Object p = entity.getProperty(name);
		if (p == null) {
			return new HashMap<K,T>();
		}
		if (p instanceof Blob) {
			return (Map<K,T>) StreamUtil.toObject(((Blob)p).getBytes());
		}
		return new HashMap<K,T>();
	}

	public static void setProperty(Entity entity, String name,
			Integer value, boolean indexed) {
		if (indexed) {
			entity.setProperty(name, value);
		}
		else {
			entity.setUnindexedProperty(name, value);
		}
	}

	public static void setProperty(Entity entity, String name,
			Long value, boolean indexed) {
		if (indexed) {
			entity.setProperty(name, value);
		}
		else {
			entity.setUnindexedProperty(name, value);
		}
	}

	public static void setProperty(Entity entity, String name, String value,
			boolean indexed) {
		if (indexed) {
			entity.setProperty(name, value);
		}
		else {
			entity.setUnindexedProperty(name, value);
		}
	}

	public static void setTextProperty(Entity entity, String name, 
			String value) {
		if (value == null) {
			entity.setProperty(name, null);
		}
		else {
			entity.setProperty(name, new Text(value));
		}
	}

	public static void setProperty(Entity entity, String name,
			Date value, boolean indexed) {
		if (indexed) {
			entity.setProperty(name, value);
		}
		else {
			entity.setUnindexedProperty(name, value); 
		}
	}

	public static void setProperty(Entity entity, String name,
			boolean value, boolean indexed) {
		if (indexed) {
			entity.setProperty(name, value);
		}
		else {
			entity.setUnindexedProperty(name, value);
		}
	}

	public static void setProperty(Entity entity, String name, byte[] value) {
		if (value == null) {
			entity.setProperty(name, null);
		}
		else {
			entity.setProperty(name, new Blob(value));
		}
	}

	public static void setProperty(Entity entity, String name, List value) {
		if (value == null) {
			entity.setUnindexedProperty(name, null);
		}
		else {
			entity.setUnindexedProperty(name, value);
		}
	}
	
	public static void setProperty(Entity entity, String name, Map value) {
		if (value == null) {
			entity.setUnindexedProperty(name, null);
		}
		else {
			entity.setUnindexedProperty(name, 
					new Blob(StreamUtil.toBytes(value)));
		}
	}
}
