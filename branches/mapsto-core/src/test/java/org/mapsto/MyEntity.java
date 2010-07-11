package org.mapsto;

public class MyEntity extends AbstractEntity {

	private String name;

	public MyEntity() {
	}

	public MyEntity(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
}
