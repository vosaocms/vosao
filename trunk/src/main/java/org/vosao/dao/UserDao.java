package org.vosao.dao;

import java.util.List;

import org.vosao.entity.UserEntity;
import org.vosao.enums.UserRole;

public interface UserDao extends AbstractDao {

	void save(final UserEntity entity);
	
	UserEntity getById(final Long id);

	UserEntity getByEmail(final String email);

	List<UserEntity> getByRole(final UserRole role);

	List<UserEntity> select();
	
	void remove(final Long id);
	
	void remove(final List<Long> ids);
	
	
}
