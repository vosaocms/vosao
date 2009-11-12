package org.vosao.update;

import java.util.Date;

import org.vosao.dao.Dao;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.LanguageEntity;
import org.vosao.entity.PageEntity;
import org.vosao.enums.PageState;
import org.vosao.utils.UrlUtil;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;


public class UpdateTask004 implements UpdateTask {

	private Dao dao;
	private DatastoreService datastore;
	
	private Dao getDao() {
		return dao;
	}
	
	public UpdateTask004(Dao aDao) {
		dao = aDao;
	}
	
	@Override
	public String getFromVersion() {
		return "0.0.3";
	}

	@Override
	public String getToVersion() {
		return "0.0.4";
	}

	@Override
	public void update() throws UpdateException {
		datastore = DatastoreServiceFactory.getDatastoreService(); 
		addEngLanguage();
		updatePages();
	}

	private void addEngLanguage() {
		LanguageEntity lang = new LanguageEntity();
		lang.setCode(LanguageEntity.ENGLISH_CODE);
		lang.setTitle(LanguageEntity.ENGLISH_TITLE);
		getDao().getLanguageDao().save(lang);
	}
	
	private void updatePages() {
		Query query = new Query("PageEntity");
		Long userId = getUserId();
		for (Entity e : datastore.prepare(query).asIterable()) {
			ContentEntity contentEntity = new ContentEntity(
					PageEntity.class.getName(),
					(String) e.getProperty("id"), 
					LanguageEntity.ENGLISH_CODE, 
					((Text) e.getProperty("content")).getValue());
			getDao().getContentDao().save(contentEntity);
			e.removeProperty("content");
			e.setProperty("version", new Integer(1));
			e.setProperty("versionTitle", "1 version");
			e.setProperty("state", PageState.APPROVED);
			e.setProperty("createUserId", userId);
			e.setProperty("modUserId", userId);
			Date dt = (Date) e.getProperty("publishDate");
			if (dt == null) {
				dt = new Date();
			}
			e.setProperty("createDate", dt);
			e.setProperty("modDate", dt);
			String friendlyUrl = (String) e.getProperty("friendlyURL");
			if (friendlyUrl.equals("/")) {
				e.setProperty("parentUrl", null);
			}
			else {
				e.setProperty("parentUrl", UrlUtil.getParentFriendlyURL(
						friendlyUrl));
			}
			datastore.put(e);
		}
	}
	
	private Long getUserId() {
		Query query = new Query("UserEntity");
		for (Entity e : datastore.prepare(query).asIterable()) {
			return (Long) e.getProperty("id");
		}
		return 1L;
	}

}
