package org.vosao.update;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;


public class UpdateTask003 implements UpdateTask {

	@Override
	public String getFromVersion() {
		return "0.0.2";
	}

	@Override
	public String getToVersion() {
		return "0.0.3";
	}

	@Override
	public void update() throws UpdateException {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService(); 
		Query query = new Query("ConfigEntity");
		for (Entity e : datastore.prepare(query).asIterable()) {
			e.setProperty("enableRecaptcha", "false");
			datastore.put(e);
		}
		query = new Query("FormEntity");
		for (Entity e : datastore.prepare(query).asIterable()) {
			e.setProperty("sendButtonTitle", "");
			e.setProperty("showResetButton", "false");
			e.setProperty("resetButtonTitle", "");
			e.setProperty("enableCaptcha", "false");
			datastore.put(e);
		}
		
	}

}
