/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * Copyright (C) 2009 Vosao development team
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.business.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.weaver.Iterators.Getter;
import org.dom4j.DocumentException;
import org.vosao.business.Business;
import org.vosao.business.ImportExportBusiness;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.business.impl.imex.ConfigExporter;
import org.vosao.business.impl.imex.PageExporter;
import org.vosao.business.impl.imex.ResourceExporter;
import org.vosao.business.impl.imex.SiteExporter;
import org.vosao.business.impl.imex.ThemeExporter;
import org.vosao.business.impl.imex.dao.DaoTaskAdapter;
import org.vosao.dao.DaoTaskException;
import org.vosao.dao.Dao;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.TemplateEntity;

public class ImportExportBusinessImpl extends AbstractBusinessImpl 
	implements ImportExportBusiness {

	private static final Log logger = LogFactory.getLog(ImportExportBusinessImpl.class);
	
	private Business business;
	private Dao dao;
	private DaoTaskAdapter daoTaskAdapter;
	
	private ThemeExporter createThemeExporter() {
		return new ThemeExporter(getDao(), getBusiness(), getDaoTaskAdapter());
	}
	
	private ResourceExporter createResourceExporter() {
		return new ResourceExporter(getDao(), getBusiness(), getDaoTaskAdapter());
	}

	private ConfigExporter createConfigExporter() {
		return new ConfigExporter(getDao(), getBusiness(), getDaoTaskAdapter());
	}

	private SiteExporter createSiteExporter() {
		return new SiteExporter(getDao(), getBusiness(), getDaoTaskAdapter());
	}

	private PageExporter createPageExporter() {
		return new PageExporter(getDao(), getBusiness(), getDaoTaskAdapter());
	}

	@Override
	public byte[] createExportFile(final List<TemplateEntity> list) 
			throws IOException {
		
		ThemeExporter themeExporter = createThemeExporter();
		
		ByteArrayOutputStream outData = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(outData);
		out.putNextEntry(new ZipEntry(ThemeExporter.THEME_FOLDER));
		for (TemplateEntity theme : list) {
			themeExporter.exportTheme(out, theme);
		}
		out.close();
		return outData.toByteArray();
	}

	public void importZip(ZipInputStream in) throws IOException, 
			DocumentException, DaoTaskException {
		
		ResourceExporter resourceExporter = createResourceExporter();
		ThemeExporter themeExporter = createThemeExporter();
		SiteExporter siteExporter = createSiteExporter();
		
		List<String> result = new ArrayList<String>();
		ZipEntry entry;
		byte[] buffer = new byte[4096];
		boolean skipping = getDaoTaskAdapter().getCurrentFile() != null;
		while((entry = in.getNextEntry()) != null) {
			if (skipping) {
				if (entry.getName().equals(getDaoTaskAdapter().getCurrentFile())) {
					skipping = false;
				}
				else {
					continue;
				}
			}
			getDaoTaskAdapter().setCurrentFile(entry.getName());
			if (entry.isDirectory()) {
				getBusiness().getFolderBusiness().createFolder(
						"/" + entry.getName());
			}
			else {
				ByteArrayOutputStream data = new ByteArrayOutputStream();
				int len = 0;
				while ((len = in.read(buffer)) > 0) {
					data.write(buffer, 0, len);
				}
				if (siteExporter.isSiteContent(entry)) {
					siteExporter.readSiteContent(entry, data.toString("UTF-8"));
				}
				else if (themeExporter.isThemeDescription(entry)) {
					themeExporter.createThemeByDescription(entry, 
							data.toString("UTF-8"));
				}
				else if (themeExporter.isThemeContent(entry)) {
					themeExporter.createThemeByContent(entry, 
							data.toString("UTF-8"));
				}
				else {
					result.add(resourceExporter.importResourceFile(
							entry, data.toByteArray()));
				}
			}
			getDaoTaskAdapter().reset();
		}
		clearResourcesCache(result);
	}
	
	private void clearResourcesCache(List<String> files) {
		for (String file : files) {
			getBusiness().getSystemService().getFileCache()
					.remove(file);
			logger.debug("Clear cache " + file);
		}
	}
	
	@Override
	public byte[] createSiteExportFile() throws IOException {
		ThemeExporter themeExporter = createThemeExporter();
		SiteExporter siteExporter = createSiteExporter();
		ByteArrayOutputStream outData = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(outData);
		out.putNextEntry(new ZipEntry(ThemeExporter.THEME_FOLDER));
		List<TemplateEntity> list = getDao().getTemplateDao().select();
		for (TemplateEntity theme : list) {
			themeExporter.exportTheme(out, theme);
		}
		siteExporter.exportSite(out);
		out.close();
		return outData.toByteArray();
	}
	
	@Override
	public byte[] createExportFile(FolderEntity folder) throws IOException {
		ResourceExporter resourceExporter = createResourceExporter();
		
		ByteArrayOutputStream outData = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(outData);
		out.putNextEntry(new ZipEntry(ThemeExporter.THEME_FOLDER));
		TreeItemDecorator<FolderEntity> root = getBusiness()
				.getFolderBusiness().getTree();
		TreeItemDecorator<FolderEntity> exportFolder = root.find(folder);
		if (exportFolder != null) {
			String zipPath = removeRootSlash(getBusiness().getFolderBusiness()
					.getFolderPath(folder, root)) + "/";
			resourceExporter.addResourcesFromFolder(out, exportFolder, zipPath);
			out.close();
			return outData.toByteArray();
		}
		else {
			logger.error("folder decorator was not found " + folder.getName());
			return null;
		}
	}
	
	private String removeRootSlash(final String path) {
		if (path.length() > 0 && path.charAt(0) == '/') {
			return path.substring(1);
		}
		return path;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	public DaoTaskAdapter getDaoTaskAdapter() {
		return daoTaskAdapter;
	}

	public void setDaoTaskAdapter(DaoTaskAdapter daoTaskAdapter) {
		this.daoTaskAdapter = daoTaskAdapter;
	}

	@Override
	public byte[] createFullExportFile() throws IOException {
		ThemeExporter themeExporter = createThemeExporter();
		SiteExporter siteExporter = createSiteExporter();
		ByteArrayOutputStream outData = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(outData);
		out.putNextEntry(new ZipEntry(ThemeExporter.THEME_FOLDER));
		out.closeEntry();
		List<TemplateEntity> list = getDao().getTemplateDao().select();
		for (TemplateEntity theme : list) {
			themeExporter.exportTheme(out, theme);
		}
		siteExporter.exportSite(out);
		exportResources(out);
		out.close();
		return outData.toByteArray();
	}

	private void exportResources(ZipOutputStream out) throws IOException {
		TreeItemDecorator<FolderEntity> root = getBusiness()
			.getFolderBusiness().getTree();
		for (TreeItemDecorator<FolderEntity> child : root.getChildren()) {
			if (!isSkipFolder(child.getEntity().getName())) {
				getResourceExporter().addResourcesFromFolder(out, child, 
						child.getEntity().getName() + "/");
			}			
		}
	}
	
	private static String[] skipFolder = {"page", "theme", "tmp"};
	
	private boolean isSkipFolder(String path) {
		for (String s : skipFolder) {
			if (path.startsWith(s)) {
				return true;
			}
		}
		return false;
	}

	private ResourceExporter resourceExporter;
	
	private ResourceExporter getResourceExporter() {
		if (resourceExporter == null) {
			resourceExporter = new ResourceExporter(getDao(), getBusiness(),
					getDaoTaskAdapter());
		}
		return resourceExporter;
	}

	@Override
	public byte[] createResourcesExportFile() throws IOException {
		ByteArrayOutputStream outData = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(outData);
		exportResources(out);
		out.close();
		return outData.toByteArray();
	}
	
	
}
