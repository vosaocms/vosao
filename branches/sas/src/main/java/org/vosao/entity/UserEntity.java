package org.vosao.entity;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.vosao.enums.UserRole;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 2L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	
	@Persistent
	private String name;
	
	@Persistent
	private String password;

	@Persistent
	private String email;

	@Persistent
	private UserRole role;
	
	public UserEntity() {
	}
	
	public UserEntity(String aName, String aPassword,
			String anEmail, UserRole aRole) {
		this();
		name = aName;
		password = aPassword;
		email = anEmail;
		role = aRole;
	}
	
	public void copy(final UserEntity entity) {
		setName(entity.getName());
		setPassword(entity.getPassword());
		setEmail(entity.getEmail());
		setRole(entity.getRole());		
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String aPassword) {
		password = aPassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}
	
}
