package org.vosao.entity.helper;

import java.util.Comparator;

import org.vosao.entity.FieldEntity;

public class FieldHelper {

	public static class IndexAsc implements Comparator<FieldEntity> {
		@Override
		public int compare(FieldEntity o1, FieldEntity o2) {
			if (o1.getIndex() > o2.getIndex()) {
				return 1;
			}
			if (o1.getIndex() == o2.getIndex()) {
				return 0;
			}
			return -1;
		}
	}
}
