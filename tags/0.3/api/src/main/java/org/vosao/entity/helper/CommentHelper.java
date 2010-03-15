package org.vosao.entity.helper;

import java.util.Comparator;

import org.vosao.entity.CommentEntity;

public class CommentHelper {

	public static class PublishDateDesc implements Comparator<CommentEntity> {
		@Override
		public int compare(CommentEntity o1, CommentEntity o2) {
			if (o1.getPublishDate().after(o2.getPublishDate())) {
				return -1;
			}
			if (o1.getPublishDate().equals(o2.getPublishDate())) {
				return 0;
			}
			return 1;
		}
	}
	
}
