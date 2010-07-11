package org.mapsto;

import java.io.Serializable;

public interface Entity extends Serializable {

	Long getId();

	void setId(Long id);
	
	Long getKey();

	void setKey(Long id);
	
	Entity copy(Entity entity);
}
