package org.vosao.entity.helper;

import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.entity.PageEntity;

public class PageHelper {

	private static final Log logger = LogFactory.getLog(PageHelper.class);
	
	private static class PublishDateDesc implements Comparator<PageEntity> {
		@Override
		public int compare(PageEntity o1, PageEntity o2) {
			if (o1.getPublishDate().after(o2.getPublishDate())) {
				return -1;
			}
			if (o1.getPublishDate().equals(o2.getPublishDate())) {
				return 0;
			}
			return 1;
		}
	}
	
	public static Comparator<PageEntity> PUBLISH_DATE_ASC = new PublishDateDesc();
	
	private static class VersionAsc implements Comparator<PageEntity> {
		@Override
		public int compare(PageEntity o1, PageEntity o2) {
			if (o1.getVersion() > o2.getVersion()) {
				return 1;
			}
			if (o1.getVersion().equals(o2.getVersion())) {
				return 0;
			}
			return -1;
		}
	}

	public static Comparator<PageEntity> VERSION_ASC = new VersionAsc();
	
	private static class SortIndexAsc implements Comparator<PageEntity> {
		@Override
		public int compare(PageEntity o1, PageEntity o2) {
			if (o1.getSortIndex() > o2.getSortIndex()) {
				return 1;
			}
			if (o1.getSortIndex().equals(o2.getSortIndex())) {
				return 0;
			}
			return -1;
		}
	}
	
	public static Comparator<PageEntity> SORT_INDEX_ASC = new SortIndexAsc();
	
}
