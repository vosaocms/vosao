package org.vosao.business.impl.imex;

import org.vosao.business.Business;
import org.vosao.business.imex.ConfigExporter;
import org.vosao.business.imex.ExporterFactory;
import org.vosao.business.imex.FolderExporter;
import org.vosao.business.imex.FormExporter;
import org.vosao.business.imex.GroupExporter;
import org.vosao.business.imex.MessagesExporter;
import org.vosao.business.imex.PageExporter;
import org.vosao.business.imex.PagePermissionExporter;
import org.vosao.business.imex.PluginExporter;
import org.vosao.business.imex.ResourceExporter;
import org.vosao.business.imex.SeoUrlExporter;
import org.vosao.business.imex.SiteExporter;
import org.vosao.business.imex.StructureExporter;
import org.vosao.business.imex.TagExporter;
import org.vosao.business.imex.ThemeExporter;
import org.vosao.business.imex.UserExporter;
import org.vosao.business.imex.task.DaoTaskAdapter;
import org.vosao.common.AbstractServiceBeanImpl;

public class ExporterFactoryImpl extends AbstractServiceBeanImpl 
		implements ExporterFactory {

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
	private SeoUrlExporter seoUrlExporter;
	private TagExporter tagExporter;
	
	public ExporterFactoryImpl(Business aBusiness, 
			DaoTaskAdapter aDaoTaskAdapter) {
		super(aBusiness);
		daoTaskAdapter = aDaoTaskAdapter;
	}

	public ConfigExporter getConfigExporter() {
		if (configExporter == null) {
			configExporter = new ConfigExporterImpl(this);
		}
		return configExporter;
	}

	public DaoTaskAdapter getDaoTaskAdapter() {
		return daoTaskAdapter;
	}

	public FolderExporter getFolderExporter() {
		if (folderExporter == null) {
			folderExporter = new FolderExporterImpl(this);
		}
		return folderExporter;
	}

	public FormExporter getFormExporter() {
		if (formExporter == null) {
			formExporter = new FormExporterImpl(this);
		}
		return formExporter;
	}

	public GroupExporter getGroupExporter() {
		if (groupExporter == null) {
			groupExporter = new GroupExporterImpl(this);
		}
		return groupExporter;
	}

	public MessagesExporter getMessagesExporter() {
		if (messagesExporter == null) {
			messagesExporter = new MessagesExporterImpl(this);
		}
		return messagesExporter;
	}

	public PageExporter getPageExporter() {
		if (pageExporter == null) {
			pageExporter = new PageExporterImpl(this);
		}
		return pageExporter;
	}

	public PagePermissionExporter getPagePermissionExporter() {
		if (pagePermissionExporter == null) {
			pagePermissionExporter = new PagePermissionExporterImpl(this);
		}
		return pagePermissionExporter;
	}

	public PluginExporter getPluginExporter() {
		if (pluginExporter == null) {
			pluginExporter = new PluginExporterImpl(this);
		}
		return pluginExporter;
	}

	public ResourceExporter getResourceExporter() {
		if (resourceExporter == null) {
			resourceExporter = new ResourceExporterImpl(this);
		}
		return resourceExporter;
	}

	public SiteExporter getSiteExporter() {
		if (siteExporter == null) {
			siteExporter = new SiteExporterImpl(this);
		}
		return siteExporter;
	}

	public StructureExporter getStructureExporter() {
		if (structureExporter == null) {
			structureExporter = new StructureExporterImpl(this);
		}
		return structureExporter;
	}

	public ThemeExporter getThemeExporter() {
		if (themeExporter == null) {
			themeExporter = new ThemeExporterImpl(this);
		}
		return themeExporter;
	}

	public UserExporter getUserExporter() {
		if (userExporter == null) {
			userExporter = new UserExporterImpl(this);
		}
		return userExporter;
	}

	public SeoUrlExporter getSeoUrlExporter() {
		if (seoUrlExporter == null) {
			seoUrlExporter = new SeoUrlsExporterImpl(this);
		}
		return seoUrlExporter;
	}

	@Override
	public TagExporter getTagExporter() {
		if (tagExporter == null) {
			tagExporter = new TagExporterImpl(this);
		}
		return tagExporter;
	}
	
}
