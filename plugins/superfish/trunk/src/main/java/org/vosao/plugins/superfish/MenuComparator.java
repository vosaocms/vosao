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

package org.vosao.plugins.superfish;

import java.util.Comparator;
import java.util.Map;

import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.PageEntity;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class MenuComparator implements 
		Comparator<TreeItemDecorator<PageEntity>> {
	
	private Map<String, Integer> enabledPages;
	
	public MenuComparator(Map<String, Integer> enabledPages) {
		super();
		this.enabledPages = enabledPages;
	}

	@Override
	public int compare(TreeItemDecorator<PageEntity> arg0, 
			TreeItemDecorator<PageEntity> arg1) {
		Integer index0 = 0, index1 = 0;
		if (enabledPages.keySet().contains(
				arg0.getEntity().getFriendlyURL())) {
			index0 = enabledPages.get(
					arg0.getEntity().getFriendlyURL());
		}
		if (enabledPages.keySet().contains(
				arg1.getEntity().getFriendlyURL())) {
			index1 = enabledPages.get(
					arg1.getEntity().getFriendlyURL());
		}
		if (index0 > index1) {
			return 1;
		}
		if (index0 < index1) {
			return -1;
		}
		return 0;
	}

}
