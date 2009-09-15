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

	public TreeItemDecorator<T> getParent() {
		return parent;
	}

	public void setParent(TreeItemDecorator<T> parent) {
		this.parent = parent;
	}
	
	
}
