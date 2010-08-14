package org.vosao.plugins.sitemap;

import java.util.List;

public class SitemapConfig {

	private int level;
	private List<String> exclude;
	
	public SitemapConfig() {	
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public List<String> getExclude() {
		return exclude;
	}
	
	public void setExclude(List<String> exclude) {
		this.exclude = exclude;
	}
}
