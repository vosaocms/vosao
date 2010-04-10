package org.vosao.update;

import org.vosao.dao.Dao;
import org.vosao.entity.PluginEntity;

public class UpdateTask04 implements UpdateTask {

	private Dao dao;
	
	public UpdateTask04(Dao aDao) {
		dao = aDao;
	}
	
	private Dao getDao() {
		return dao;
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
		updatePlugins();
	}

	private void updatePlugins() {
		for (PluginEntity plugin : getDao().getPluginDao().select()) {
			plugin.setDisabled(true);
			getDao().getPluginDao().save(plugin);
		}
	}
	
}
