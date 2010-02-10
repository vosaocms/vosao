package org.vosao.plugins.superfish;

import java.util.Comparator;
import java.util.Map;

import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.PageEntity;

public class MenuComparator implements 
		Comparator<TreeItemDecorator<PageEntity>> {
	
	private Map<String, Integer> enabledPages;
	
	public MenuComparator(Map<String, Integer> enabledPages) {
		super();
		this.enabledPages = enabledPages;
	}

	@Override
	public int compare(TreeItemDecorator<PageEntity> arg0, 
			TreeItemDecorator<PageEntity> arg1) {
		Integer index0 = 0, index1 = 0;
		if (enabledPages.keySet().contains(
				arg0.getEntity().getFriendlyURL())) {
			index0 = enabledPages.get(
					arg0.getEntity().getFriendlyURL());
		}
		if (enabledPages.keySet().contains(
				arg1.getEntity().getFriendlyURL())) {
			index1 = enabledPages.get(
					arg1.getEntity().getFriendlyURL());
		}
		if (index0 > index1) {
			return 1;
		}
		if (index0 < index1) {
			return -1;
		}
		return 0;
	}

}
