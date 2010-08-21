/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

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
	
	public static Comparator<PageEntity> PUBLISH_DATE = new PublishDateDesc();
	
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
