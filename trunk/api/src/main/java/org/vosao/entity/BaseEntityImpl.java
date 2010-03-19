package org.vosao.entity;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.utils.DateUtil;
import org.vosao.utils.EntityUtil;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

public abstract class BaseEntityImpl implements BaseEntity {

	protected static final Log logger = LogFactory.getLog(
			BaseEntityImpl.class);

	private Key key;
	private String createUserEmail;
	private Date createDate;
	private String modUserEmail;
	private Date modDate;

	public BaseEntityImpl() {
		createDate = new Date();
		modDate = createDate;
		createUserEmail = "";
		modUserEmail = "";
	}
	
	@Override
	public Long getId() {
		return key == null ? null : key.getId();
	}

	@Override
	public void setId(Long id) {
		if (id != null && id > 0) {
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
		createUserEmail = getStringProperty(entity, "createUserEmail");
		createDate = getDateProperty(entity, "createDate");
		modUserEmail = getStringProperty(entity, "modUserEmail");
		modDate = getDateProperty(entity, "modDate");
	}

	@Override
	public void save(Entity entity) {
		entity.setUnindexedProperty("createUserEmail", createUserEmail);
		entity.setUnindexedProperty("createDate", createDate);
		entity.setUnindexedProperty("modUserEmail", modUserEmail);
		entity.setUnindexedProperty("modDate", modDate);
	}

	@Override
	public boolean isNew() {
		return key == null;
	}
	
	@Override
	public void copy(BaseEntity entity) {
		Key myKey = getKey(); 
		Entity buf = new Entity("tmp");
		entity.save(buf);
		load(buf);
		setKey(myKey);
	}
	
	public boolean equals(Object object) {
		if (object instanceof BaseEntity
				&& object.getClass().equals(this.getClass())) {
			BaseEntity entity = (BaseEntity) object;
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

	@Override
	public String getCreateUserEmail() {
		return createUserEmail;
	}

	@Override
	public void setCreateUserEmail(String createUserEmail) {
		this.createUserEmail = createUserEmail;
	}

	@Override
	public Date getCreateDate() {
		return createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String getModUserEmail() {
		return modUserEmail;
	}

	@Override
	public void setModUserEmail(String modUserEmail) {
		this.modUserEmail = modUserEmail;
	}

	@Override
	public Date getModDate() {
		return modDate;
	}

	@Override
	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	@Override
	public String getModDateString() {
		return DateUtil.toString(modDate);
	}

	@Override
	public String getCreateDateString() {
		return DateUtil.toString(createDate);
	}
	
}
