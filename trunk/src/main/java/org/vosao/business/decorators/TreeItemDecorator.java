package org.vosao.business.decorators;

import java.util.ArrayList;
import java.util.List;

public class TreeItemDecorator<T> {

	private T entity;
	private List<TreeItemDecorator<T>> children;
	
	public TreeItemDecorator(T anEntity) {
		super();
		this.entity = anEntity;
		this.children = new ArrayList<TreeItemDecorator<T>>();
	}

	public TreeItemDecorator(T anEntity, List<TreeItemDecorator<T>> children) {
		super();
		this.entity = anEntity;
		this.children = children;
	}
	
	public T getEntity() {
		return entity;
	}
	
	public List<TreeItemDecorator<T>> getChildren() {
		return children;
	}
	
	
}
