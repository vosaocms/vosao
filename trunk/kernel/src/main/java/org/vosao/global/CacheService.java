package org.vosao.global;

import javax.cache.Cache;

public interface CacheService extends Cache {

	void resetLocalCache();
	
}
