package org.vosao.update;

import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.entity.PluginEntity;

public class UpdateTask04 implements UpdateTask {

	private Business business;
	
	public UpdateTask04(Business aBusiness) {
		business = aBusiness;
	}
	
	private Dao getDao() {
		return business.getDao();
	}
	
	private Business getBusiness() {
		return business;
	}

	@Override
	public String getFromVersion() {
		return "0.3";
	}

	@Override
	public String getToVersion() {
		return "0.4";
	}

	@Override
	public void update() throws UpdateException {
		getBusiness().getSetupBean().clearSessions();
		updatePlugins();
	}

	private void updatePlugins() {
		for (PluginEntity plugin : getDao().getPluginDao().select()) {
			plugin.setDisabled(true);
			getDao().getPluginDao().save(plugin);
		}
	}
	
	
}
