package org.vosao.dao.cache;

public class CacheStat {
	
	private Long calls;
	private Long hits;
	
	public CacheStat(Long calls, Long hits) {
		super();
		this.calls = calls;
		this.hits = hits;
	}

	public Long getCalls() {
		return calls;
	}

	public Long getHits() {
		return hits;
	}
	
	public String toString() {
		return "calls:" + calls + " hits:" + hits + " " 
				+ (calls == 0 ? 0 : (hits*100/calls)) + "%";
	}
}
