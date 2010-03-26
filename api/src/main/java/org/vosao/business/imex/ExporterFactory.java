package org.vosao.business.imex;

import org.vosao.business.imex.task.DaoTaskAdapter;
import org.vosao.common.AbstractServiceBean;

public interface ExporterFactory extends AbstractServiceBean {

	ConfigExporter getConfigExporter();

	DaoTaskAdapter getDaoTaskAdapter();

	FolderExporter getFolderExporter();

	FormExporter getFormExporter();

	GroupExporter getGroupExporter();

	MessagesExporter getMessagesExporter();

	PageExporter getPageExporter();

	PagePermissionExporter getPagePermissionExporter();

	PluginExporter getPluginExporter();

	ResourceExporter getResourceExporter();

	SiteExporter getSiteExporter();

	StructureExporter getStructureExporter();

	ThemeExporter getThemeExporter();

	UserExporter getUserExporter();
	
	SeoUrlExporter getSeoUrlExporter();
}
