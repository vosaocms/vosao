package org.vosao.entity.helper;

import java.util.ArrayList;
import java.util.List;

import org.vosao.entity.ContentEntity;

public class ContentHelper {

	public static List<Long> createIdList(
			final List<ContentEntity> list) {
		List<Long> result = new ArrayList<Long>();
		for (ContentEntity entity : list) {
			result.add(entity.getId());
		}
		return result;
	}
	
}
