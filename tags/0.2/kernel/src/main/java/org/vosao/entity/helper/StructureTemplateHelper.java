package org.vosao.entity.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vosao.entity.StructureTemplateEntity;

public class StructureTemplateHelper {

	public static Map<String, StructureTemplateEntity> createIdMap(
			final List<StructureTemplateEntity> list) {
		Map<String, StructureTemplateEntity> result = 
				new HashMap<String, StructureTemplateEntity>();
		for (StructureTemplateEntity entity : list) {
			result.put(entity.getId(), entity);
		}
		return result;
	}
	
	public static List<String> createIdList(
			final List<StructureTemplateEntity> list) {
		List<String> result = new ArrayList<String>();
		for (StructureTemplateEntity entity : list) {
			result.add(entity.getId());
		}
		return result;
	}
	
}
