package org.vosao.entity.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vosao.entity.UserEntity;
import org.vosao.enums.UserRole;

public class UserHelper {

	public static UserEntity GUEST = new UserEntity("guest","","",
			UserRole.GUEST);

	public static Map<Long, UserEntity> getMap(List<UserEntity> list) {
		Map<Long, UserEntity> map = new HashMap<Long, UserEntity>();
		for (UserEntity user : list) {
			map.put(user.getId(), user);
		}
		return map;
	}
	
}
