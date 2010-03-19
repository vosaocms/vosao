package org.vosao.business.impl.imex;

import org.vosao.business.Business;
import org.vosao.business.impl.imex.task.DaoTaskAdapter;
import org.vosao.common.AbstractServiceBean;

public class ExporterFactory extends AbstractServiceBean {

	private DaoTaskAdapter daoTaskAdapter;
	
	private ConfigExporter configExporter;
	private FolderExporter folderExporter;
	private FormExporter formExporter;
	private GroupExporter groupExporter;
	private MessagesExporter messagesExporter;
	private PageExporter pageExporter;
	private PagePermissionExporter pagePermissionExporter;
	private PluginExporter pluginExporter;
	private ResourceExporter resourceExporter;
	private SiteExporter siteExporter;
	private StructureExporter structureExporter;
	private ThemeExporter themeExporter;
	private UserExporter userExporter;
	
	public ExporterFactory(Business aBusiness, DaoTaskAdapter aDaoTaskAdapter) {
		super(aBusiness);
		daoTaskAdapter = aDaoTaskAdapter;
	}

	public ConfigExporter getConfigExporter() {
		if (configExporter == null) {
			configExporter = new ConfigExporter(this);
		}
		return configExporter;
	}

	public DaoTaskAdapter getDaoTaskAdapter() {
		return daoTaskAdapter;
	}

	public FolderExporter getFolderExporter() {
		if (folderExporter == null) {
			folderExporter = new FolderExporter(this);
		}
		return folderExporter;
	}

	public FormExporter getFormExporter() {
		if (formExporter == null) {
			formExporter = new FormExporter(this);
		}
		return formExporter;
	}

	public GroupExporter getGroupExporter() {
		if (groupExporter == null) {
			groupExporter = new GroupExporter(this);
		}
		return groupExporter;
	}

	public MessagesExporter getMessagesExporter() {
		if (messagesExporter == null) {
			messagesExporter = new MessagesExporter(this);
		}
		return messagesExporter;
	}

	public PageExporter getPageExporter() {
		if (pageExporter == null) {
			pageExporter = new PageExporter(this);
		}
		return pageExporter;
	}

	public PagePermissionExporter getPagePermissionExporter() {
		if (pagePermissionExporter == null) {
			pagePermissionExporter = new PagePermissionExporter(this);
		}
		return pagePermissionExporter;
	}

	public PluginExporter getPluginExporter() {
		if (pluginExporter == null) {
			pluginExporter = new PluginExporter(this);
		}
		return pluginExporter;
	}

	public ResourceExporter getResourceExporter() {
		if (resourceExporter == null) {
			resourceExporter = new ResourceExporter(this);
		}
		return resourceExporter;
	}

	public SiteExporter getSiteExporter() {
		if (siteExporter == null) {
			siteExporter = new SiteExporter(this);
		}
		return siteExporter;
	}

	public StructureExporter getStructureExporter() {
		if (structureExporter == null) {
			structureExporter = new StructureExporter(this);
		}
		return structureExporter;
	}

	public ThemeExporter getThemeExporter() {
		if (themeExporter == null) {
			themeExporter = new ThemeExporter(this);
		}
		return themeExporter;
	}

	public UserExporter getUserExporter() {
		if (userExporter == null) {
			userExporter = new UserExporter(this);
		}
		return userExporter;
	}
	
}
