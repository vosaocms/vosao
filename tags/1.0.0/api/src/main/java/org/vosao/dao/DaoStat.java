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

package org.vosao.dao;

public class DaoStat {

	private Long getCalls = 0L;
	private Long queryCalls = 0L;
	private Long entityCacheHits = 0L;
	private Long queryCacheHits = 0L;
	
	public DaoStat() {
	}

	public DaoStat clone() {
		DaoStat result = new DaoStat();
		result.getCalls = getCalls;
		result.entityCacheHits = entityCacheHits;
		result.queryCacheHits = queryCacheHits;
		result.queryCalls = queryCalls;
		return result;
	}
	
	public Long getGetCalls() {
		return getCalls;
	}
	
	public void incGetCalls() {
		getCalls++;
	}

	public Long getQueryCalls() {
		return queryCalls;
	}
	
	public void incQueryCalls() {
		queryCalls++;
	}

	public String toString() {
		return "getCalls:" + getCalls + " queryCalls:" + queryCalls; 
	}

	public Long getEntityCacheHits() {
		return entityCacheHits;
	}

	public void incEntityCacheHits() {
		entityCacheHits++;
	}

	public Long getQueryCacheHits() {
		return queryCacheHits;
	}

	public void incQueryCacheHits() {
		queryCacheHits++;
	}
}
