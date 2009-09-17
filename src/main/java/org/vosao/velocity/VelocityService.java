package org.vosao.velocity;

import java.util.List;

import org.vosao.entity.PageEntity;

public interface VelocityService {

	PageEntity findPage(final String path);
	
	List<PageEntity> findPageChildren(final String path);

}
