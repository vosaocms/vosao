package org.mapsto;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

public class AbstractEntity implements Entity {

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getKey() {
		return id;
	}

	public void setKey(Long id) {
		this.id = id;
	}

	@Override
	public Entity copy(Entity entity) {
		try {
			BeanUtils.copyProperties(this, entity);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return this;
	}
}
