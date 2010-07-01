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

package org.vosao.business.decorators;

import java.util.ArrayList;
import java.util.List;

public class TreeItemDecorator<T> {

	private T entity;
	private List<TreeItemDecorator<T>> children;
	private TreeItemDecorator<T> parent;
	
	public TreeItemDecorator(T anEntity, TreeItemDecorator<T> aParent) {
		entity = anEntity;
		children = new ArrayList<TreeItemDecorator<T>>();
		parent = aParent;
	}

	public TreeItemDecorator(T anEntity, TreeItemDecorator<T> aParent, 
			List<TreeItemDecorator<T>> aChildren) {
		entity = anEntity;
		children = aChildren;
		parent = aParent;
	}
	
	public T getEntity() {
		return entity;
	}
	
	public List<TreeItemDecorator<T>> getChildren() {
		return children;
	}
	
	public boolean isHasChildren() {
		return children != null && children.size() > 0;
	}

	public void setChildren(List<TreeItemDecorator<T>> list) {
		children = list;
	}

	public TreeItemDecorator<T> getParent() {
		return parent;
	}

	public void setParent(TreeItemDecorator<T> parent) {
		this.parent = parent;
	}
	
	public TreeItemDecorator<T> find(final T entity) {
		if (getEntity().equals(entity)) {
			return this;
		}
		TreeItemDecorator<T> result = null;
		for (TreeItemDecorator<T> child : getChildren()) {
			result = child.find(entity);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	
}
