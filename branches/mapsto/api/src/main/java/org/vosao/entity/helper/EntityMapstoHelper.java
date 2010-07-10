package org.vosao.entity.helper;

import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.entity.BaseEntity;
import org.vosao.entity.BaseMapstoEntity;
import org.vosao.entity.PageEntity;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class EntityMapstoHelper {

	private static final Log logger = LogFactory.getLog(EntityMapstoHelper.class);
	
	private static class ModDateDesc implements Comparator<BaseMapstoEntity> {
		@Override
		public int compare(BaseMapstoEntity o1, BaseMapstoEntity o2) {
			if (o1.getModDate().after(o2.getModDate())) {
				return -1;
			}
			if (o1.getModDate().equals(o2.getModDate())) {
				return 0;
			}
			return 1;
		}
	}
	
	public static Comparator<BaseMapstoEntity> MOD_DATE_DESC = new ModDateDesc();
	
}
