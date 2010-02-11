package org.vosao.entity.helper;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.entity.PageEntity;

public class PageHelper {

	private static final Log logger = LogFactory.getLog(PageHelper.class);
	
	public static class PublishDateDesc implements Comparator<PageEntity> {
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
	
	public static class VersionAsc implements Comparator<PageEntity> {
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
	
}
