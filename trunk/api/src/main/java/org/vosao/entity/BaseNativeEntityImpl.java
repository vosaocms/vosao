package org.vosao.entity;

import org.vosao.utils.EntityUtil;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

public abstract class BaseNativeEntityImpl implements BaseNativeEntity {

	private Key key;

	@Override
	public Long getId() {
		return key == null ? null : key.getId();
	}

	@Override
	public void setId(Long id) {
		if (id != null) {
			key = KeyFactory.createKey(EntityUtil.getKind(getClass()), id);
		}
	}

	@Override
	public Key getKey() {
		return key;
	}

	@Override
	public void setKey(Key key) {
		this.key = key;
	}

	@Override
	public void load(Entity entity) {
		key = entity.getKey();
	}

	@Override
	public void save(Entity entity) {
	}

	public boolean equals(Object object) {
		if (object instanceof BaseNativeEntity
				&& object.getClass().equals(this.getClass())) {
			BaseNativeEntity entity = (BaseNativeEntity) object;
			if (getId() == null && entity.getId() == null) {
				return true;
			}
			if (getId() != null && getId().equals(entity.getId())) {
				return true;
			}
		}
		return false;
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
		return defaultValue;
	}

	public static Long getLongProperty(Entity entity, String name,
			long defaultValue) {
		Object p = entity.getProperty(name);
		if (p == null) {
			return defaultValue;
		}
		if (p instanceof Long) {
			return (Long) p;
		}
		return defaultValue;
	}

	public static String getStringProperty(Entity entity, String name) {
		Object p = entity.getProperty(name);
		if (p == null) {
			return null;
		}
		if (p instanceof String) {
			return (String) p;
		}
		return null;
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

}
