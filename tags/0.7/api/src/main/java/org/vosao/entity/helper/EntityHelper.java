package org.vosao.entity.helper;

import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.entity.BaseEntity;
import org.vosao.entity.PageEntity;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class EntityHelper {

	private static final Log logger = LogFactory.getLog(EntityHelper.class);
	
	private static class ModDateDesc implements Comparator<BaseEntity> {
		@Override
		public int compare(BaseEntity o1, BaseEntity o2) {
			if (o1.getModDate().after(o2.getModDate())) {
				return -1;
			}
			if (o1.getModDate().equals(o2.getModDate())) {
				return 0;
			}
			return 1;
		}
	}
	
	public static Comparator<BaseEntity> MOD_DATE_DESC = new ModDateDesc();
	
}
