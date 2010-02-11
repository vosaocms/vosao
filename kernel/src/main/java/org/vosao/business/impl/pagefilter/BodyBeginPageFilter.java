/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * Copyright (C) 2009 Vosao development team
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.business.impl.pagefilter;

import org.vosao.business.Business;
import org.vosao.business.impl.pagefilter.fragments.EditorPanelFragment;
import org.vosao.entity.PageEntity;

public class BodyBeginPageFilter extends AbstractPageFilter 
		implements PageFilter {

	public BodyBeginPageFilter(Business business) {
		super(business);
		getFragments().add(new EditorPanelFragment());
	}
	
	@Override
	public String apply(String content, PageEntity pageEntity) {
		return applyTag(content, pageEntity, "<body");
	}

}
