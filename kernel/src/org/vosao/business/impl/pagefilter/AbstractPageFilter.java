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

package org.vosao.business.impl.pagefilter;

import java.util.ArrayList;
import java.util.List;

import org.vosao.business.Business;
import org.vosao.entity.PageEntity;

public abstract class AbstractPageFilter implements PageFilter {

	private Business business;
	private List<ContentFragment> fragments;
	
	public AbstractPageFilter(Business business) {
		super();
		this.business = business;
		this.fragments = new ArrayList<ContentFragment>();
	}

	public Business getBusiness() {
		return business;
	}
	
	public void setBusiness(Business bean) {
		this.business = bean;
	}

	public List<ContentFragment> getFragments() {
		return fragments;
	}

	public void setFragments(List<ContentFragment> fragments) {
		this.fragments = fragments;
	}
	
	public String getFragmentsContent(PageEntity page) { 
		StringBuffer code = new StringBuffer();
		for (ContentFragment fragment : getFragments()) {
			code.append(fragment.get(getBusiness(), page)).append("\n");
		}
		return code.toString();
	}
	
	public int findTagInsertPosition(String tagName, StringBuffer page) {
		String tag = tagName.toLowerCase();
		String tagUpper = tagName.toUpperCase();
		if (page.indexOf(tagUpper) != -1) {
			tag = tagUpper;
		}
		int bodyStart = page.indexOf(tag);
		if (tagName.startsWith("</")) {
			return bodyStart;
		}
		else {
			int result = page.indexOf(">", bodyStart) + 1;
			if (result >= page.length()) {
				page.append(" ");
			}
			return result;
		}
	}

	public String applyTag(String content, PageEntity page, String tag) {
		StringBuffer buffer = new StringBuffer(content);
		int index = findTagInsertPosition(tag, buffer);
		if (index > 0) {
			buffer.insert(index, getFragmentsContent(page));
		}
		return buffer.toString();
	}
	
}
