package org.vosao.dao;

import java.util.List;

import org.vosao.entity.UserEntity;
import org.vosao.enums.UserRole;

public class UserDaoTest extends AbstractDaoTest {

	private UserEntity addUser(final String name, final String password, 
			final String email,final UserRole role) {
		UserEntity user = new UserEntity(name, password, email, role);
		getDao().getUserDao().save(user);
		return user;
	}
	
	public void testSave() {
		addUser("name", "password", "test@test.com", UserRole.USER);
		List<UserEntity> users = getDao().getUserDao().select();
		assertEquals(1, users.size());
		UserEntity user1 = users.get(0);
		assertEquals("name", user1.getName());
	}	
	
	public void testGetById() {
		UserEntity user = addUser("name","password","test@test.com", UserRole.ADMIN);
		UserEntity user2 = getDao().getUserDao().getById(user.getId());
		assertEquals(user.getName(), user2.getName());
		assertEquals(user.getPassword(), user2.getPassword());
		assertEquals(user.getEmail(), user2.getEmail());
		assertEquals(user.getRole(), user2.getRole());
	}	

	public void testSelect() {
		addUser("name1", "password1", "test1@test.com", UserRole.ADMIN);
		addUser("name2", "password2", "test2@test.com", UserRole.ADMIN);
		addUser("name3", "password3", "test3@test.com", UserRole.USER);
		List<UserEntity> users = getDao().getUserDao().select();
		assertEquals(3, users.size());
	}	
	
	public void testUpdate() {
		UserEntity user = addUser("name1", "password1", "test1@test.com", 
				UserRole.ADMIN);
		UserEntity user2 = getDao().getUserDao().getById(user.getId());
		user2.setName("name2");
		getDao().getUserDao().save(user2);
		UserEntity user3 = getDao().getUserDao().getById(user.getId());
		assertEquals("name2", user3.getName());
	}
	
	public void testResultList() {
		addUser("name1", "password1", "test1@test.com", UserRole.USER);
		addUser("name2", "password2", "test2@test.com", UserRole.USER);
		addUser("name3", "password3", "test3@test.com", UserRole.USER);
		List<UserEntity> users = getDao().getUserDao().select();
		UserEntity user = new UserEntity("name", "password", "test@test.com", UserRole.ADMIN);
		users.add(user);
		assertEquals(4, users.size());
	}

	public void testGetByEmail() {
		addUser("name1","password1","test1@test.com", UserRole.ADMIN);
		addUser("name2","password2","test2@test.com", UserRole.USER);
		addUser("name3","password3","test3@test.com", UserRole.ADMIN);
		UserEntity user1 = getDao().getUserDao().getByEmail("test2@test.com");
		assertEquals("name2", user1.getName());
		assertEquals("password2", user1.getPassword());
		assertEquals(UserRole.USER, user1.getRole());
		UserEntity user2 = getDao().getUserDao().getByEmail("test22@test.com");
		assertNull(user2);
	}	
	
	public void testGetByRole() {
		addUser("name1","password1","test1@test.com", UserRole.ADMIN);
		addUser("name2","password2","test2@test.com", UserRole.USER);
		addUser("name3","password3","test3@test.com", UserRole.ADMIN);
		List<UserEntity> users1 = getDao().getUserDao().getByRole(UserRole.ADMIN);
		assertEquals(2, users1.size());
		List<UserEntity> users2 = getDao().getUserDao().getByRole(UserRole.USER);
		assertEquals(1, users2.size());
	}	
	
}
