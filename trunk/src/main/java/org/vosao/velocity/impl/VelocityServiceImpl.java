package org.vosao.velocity.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.vosao.dao.Dao;
import org.vosao.entity.PageEntity;
import org.vosao.velocity.VelocityService;

public class VelocityServiceImpl implements VelocityService {

	private Dao dao;
	
	public VelocityServiceImpl(Dao aDao) {
		setDao(aDao);
	}
	
	private Dao getDao() {
		return dao;
	}
	
	private void setDao(Dao aDao) {
		dao = aDao;
	}
	
	@Override
	public PageEntity findPage(String path) {
		PageEntity page = getDao().getPageDao().getByUrl(path);
		if (page == null) {
			page = new PageEntity("Page not found", "Page not found", 
					"Page not found", null, null);
		}
		return page;
	}

	@Override
	public List<PageEntity> findPageChildren(String path) {
		PageEntity page = getDao().getPageDao().getByUrl(path);
		if (page == null) {
			return new ArrayList<PageEntity>();
		}
		List<PageEntity> result = getDao().getPageDao().getByParent(page.getId());
		Collections.sort(result, new Comparator<PageEntity>() {

			@Override
			public int compare(PageEntity o1, PageEntity o2) {
				Date d1 = o1.getPublishDate();
				Date d2 = o2.getPublishDate();
				if (d1 == null && d2 == null) {
					return 0;
				}
				if (d1 == null) {
					return -1;
				}
				if (d2 == null) {
					return 1;
				}
				return -1 * d1.compareTo(d2);
			}
			
		});
		return result;
	}

}
