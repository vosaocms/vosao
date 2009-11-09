package org.vosao.update;

import org.vosao.dao.Dao;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.LanguageEntity;
import org.vosao.entity.PageEntity;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;


public class UpdateTask004 implements UpdateTask {

	private Dao dao;
	
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
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService(); 
		Query query = new Query("PageEntity");
		for (Entity e : datastore.prepare(query).asIterable()) {
			ContentEntity contentEntity = new ContentEntity(
					PageEntity.class.getName(),
					(String) e.getProperty("id"), 
					LanguageEntity.ENGLISH_CODE, 
					((Text) e.getProperty("content")).getValue());
			getDao().getContentDao().save(contentEntity);
			e.removeProperty("content");
			datastore.put(e);
		}
		addEngLanguage();
	}

	private void addEngLanguage() {
		LanguageEntity lang = new LanguageEntity();
		lang.setCode(LanguageEntity.ENGLISH_CODE);
		lang.setTitle(LanguageEntity.ENGLISH_TITLE);
		getDao().getLanguageDao().save(lang);
	}
	
	

}
