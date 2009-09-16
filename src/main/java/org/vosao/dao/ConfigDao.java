package org.vosao.dao;

import java.util.List;
import java.util.Map;

import org.vosao.entity.ConfigEntity;

public interface ConfigDao extends AbstractDao {

	void save(final ConfigEntity entity);
	
	ConfigEntity getById(final Long id);

	ConfigEntity getByName(final String name);

	List<ConfigEntity> select();
	
	void remove(final Long id);
	
	void remove(final List<Long> ids);
	
	/**
	 * Get all configs with values.
	 * @return configs with values.
	 */
	Map<String, String> getConfig();
}
