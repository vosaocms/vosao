package org.vosao.entity.helper;

import java.util.ArrayList;
import java.util.List;

import org.vosao.entity.ContentEntity;

public class ContentHelper {

	public static List<String> createIdList(
			final List<ContentEntity> list) {
		List<String> result = new ArrayList<String>();
		for (ContentEntity entity : list) {
			result.add(entity.getId());
		}
		return result;
	}
	
}
