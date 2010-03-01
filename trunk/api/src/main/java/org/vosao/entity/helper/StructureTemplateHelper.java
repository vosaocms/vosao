package org.vosao.entity.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vosao.entity.StructureTemplateEntity;

public class StructureTemplateHelper {

	public static Map<Long, StructureTemplateEntity> createIdMap(
			final List<StructureTemplateEntity> list) {
		Map<Long, StructureTemplateEntity> result = 
				new HashMap<Long, StructureTemplateEntity>();
		for (StructureTemplateEntity entity : list) {
			result.put(entity.getId(), entity);
		}
		return result;
	}
	
	public static List<Long> createIdList(
			final List<StructureTemplateEntity> list) {
		List<Long> result = new ArrayList<Long>();
		for (StructureTemplateEntity entity : list) {
			result.add(entity.getId());
		}
		return result;
	}
	
}
