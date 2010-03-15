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

package org.vosao.business.impl.plugin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.Business;
import org.vosao.common.PluginException;
import org.vosao.dao.Dao;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.PluginEntity;
import org.vosao.entity.PluginResourceEntity;
import org.vosao.servlet.MimeType;
import org.vosao.utils.FolderUtil;
import org.vosao.utils.StrUtil;

public class PluginLoader {

	private static final Log logger = LogFactory.getLog(PluginLoader.class);

	private static final String VOSAO_PLUGIN = "WEB-INF/vosao-plugin.xml";
	private static final String RESOURCE_LIST = ".resourceList";
	private static final String FILE_LIST = ".fileList";
	
	private Dao dao;
	private Business business;
	
	public PluginLoader(Dao dao, Business business) {
		super();
		this.dao = dao;
		this.business = business;
	}

	private static class WarItem {
		public String path;
		public String filename;
		public ByteArrayOutputStream data;

		public WarItem(String path, ByteArrayOutputStream data) {
			super();
			this.path = path;
			this.filename = FolderUtil.getFileName(path);
			this.data = data;
		}
	}
	
	/**
	 * Plugin installation:
	 * - PluginEntity created
	 * - All resources are placed to /plugins/PLUGIN_NAME/
	 * - all classes are placed to PluginResourceEntity
	 */
	public void install(String filename, byte[] data) throws IOException, 
			PluginException, DocumentException {
		Map<String, WarItem> war = readWar(data);
		if (!war.containsKey(VOSAO_PLUGIN)) {
			throw new PluginException(VOSAO_PLUGIN + " not found");
		}
		PluginEntity plugin = readPluginConfig(war.get(VOSAO_PLUGIN));
		PluginEntity p = getDao().getPluginDao().getByName(plugin.getName());
		if (p != null) {
			throw new PluginException("Plugin " + plugin.getTitle() + " already installed.");
		}
		if (StringUtils.isEmpty(plugin.getEntryPointClass())) {
			throw new PluginException("Entry point class not defined.");
		}
		getDao().getPluginDao().save(plugin);
		String pluginBase = "/plugins/" + plugin.getName();
		getBusiness().getFolderBusiness().createFolder(pluginBase);
		List<String> resourceList = new ArrayList<String>();
		List<String> fileCacheList = new ArrayList<String>();
		String filePrefix = pluginBase + "/";
		for (String url : war.keySet()) {
			if (!url.equals(VOSAO_PLUGIN)) {
				WarItem item = war.get(url);
				byte[] fileData = item.data.toByteArray();
				if (url.startsWith("WEB-INF/classes")) {
					resourceList.add(loadClasspathResource(item, plugin));
				}
				if (!url.startsWith("WEB-INF")) {
					fileCacheList.add(filePrefix + item.path);
					String folderPath = pluginBase + "/" + FolderUtil.getFilePath(
							item.path);
					FolderEntity folder = getBusiness().getFolderBusiness()
							.createFolder(folderPath);
					FileEntity file = new FileEntity(item.filename, 
							item.filename, folder.getId(), 
							MimeType.getContentTypeByExt(
									FolderUtil.getFileExt(item.path)), 
							new Date(), fileData.length);
					getDao().getFileDao().save(file, fileData);
				}
				if (url.startsWith("WEB-INF/lib") && url.endsWith(".jar")) {
					resourceList.addAll(loadJarFile(item, plugin));
				}
			}
		}
		saveResourceList(plugin, resourceList, fileCacheList);
	}
	
	private String loadClasspathResource(WarItem item, PluginEntity plugin) {
		String ext = FolderUtil.getFileExt(item.path);
		byte[] fileData = item.data.toByteArray();
		String resourceName = item.path.replace("WEB-INF/classes/", "");
		if (ext.equals("class")) {
			resourceName = resourceName.replace('/', '.')
				.replace(".class", "");
		}
		PluginResourceEntity res = getDao().getPluginResourceDao()
				.getByUrl(plugin.getName(), resourceName);
		if (res == null) {
			res = new PluginResourceEntity(plugin.getName(),
					resourceName, fileData);
		}
		else {
			res.setContent(fileData);
		}
		getDao().getPluginResourceDao().save(res);
		getBusiness().getPluginResourceBusiness()
			.updateResourceCache(res);
		return res.getId().toString();
	}

	private List<String> loadJarFile(WarItem file, PluginEntity plugin) 
			throws IOException {
		List<String> result = new ArrayList<String>();
		Map<String, WarItem> war = readWar(file.data.toByteArray());
		for (String path : war.keySet()) {
			result.add(loadClasspathResource(war.get(path), plugin));
		}
		return result;
	}
	
	private void saveResourceList(PluginEntity plugin, 
			List<String> resourceList, List<String> fileList) {
		String resourceListStr = StrUtil.toCSV(resourceList);
		String fileListStr = StrUtil.toCSV(fileList);
		try {
			getDao().getPluginResourceDao().save(
					new PluginResourceEntity(plugin.getName(),
							plugin.getName() + RESOURCE_LIST, 
							resourceListStr.getBytes("UTF-8")));
			getDao().getPluginResourceDao().save(
					new PluginResourceEntity(plugin.getName(),
							plugin.getName() + FILE_LIST, 
							fileListStr.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private Map<String, WarItem> readWar(byte[] data) throws IOException {
		ByteArrayInputStream inputData = new ByteArrayInputStream(data);
		ZipInputStream in = new ZipInputStream(inputData);
		Map<String, WarItem> map = new HashMap<String, WarItem>();
		ZipEntry entry;
		byte[] buffer = new byte[4096];
		while((entry = in.getNextEntry()) != null) {
			if (!entry.isDirectory()) {
				ByteArrayOutputStream itemData = new ByteArrayOutputStream();
				int len = 0;
				while ((len = in.read(buffer)) > 0) {
					itemData.write(buffer, 0, len);
				}
				WarItem item = new WarItem(entry.getName(), itemData);
				map.put(entry.getName(), item);
			}
		}
		in.close();
		return map;
	}

	private PluginEntity readPluginConfig(WarItem zipItem) 
			throws UnsupportedEncodingException, DocumentException {
		PluginEntity result = new PluginEntity();
		Element root = DocumentHelper.parseText(zipItem.data.toString("UTF-8"))
				.getRootElement();
		result.setName(root.elementText("name"));	
		result.setTitle(root.elementText("title"));
		result.setDescription(root.elementText("description"));
		result.setWebsite(root.elementText("website"));
		if (root.element("entry-point-class") != null) {
			result.setEntryPointClass(StringUtils.strip(
					root.elementText("entry-point-class")));
		}
		if (root.element("plugin-config-url") != null) {
			result.setConfigURL(StringUtils.strip(
					root.elementText("plugin-config-url")));
		}
		StringBuffer header = new StringBuffer();
		if (root.element("header-javascript") != null) {
			for (Element e : (List<Element>)root.elements("header-javascript")) {
				header.append("<script type=\"text/javascript\" src=\"/file/plugins/")
					.append(result.getName()).append("/").append(e.getText())
					.append("\"></script>\n");
			}
		}
		if (root.element("header-css") != null) {
			for (Element e : (List<Element>)root.elements("header-css")) {
				header.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/file/plugins/")
					.append(result.getName()).append("/").append(e.getText())
					.append("\"/>\n");
			}
		}
		result.setPageHeader(header.toString());
		if (root.element("config") != null) {
			result.setConfigStructure(root.element("config").asXML());
		}
		return result; 
	}

	public void uninstall(PluginEntity plugin) {
		removePluginResources(plugin);
		removePluginFileCache(plugin);
		getBusiness().getFolderBusiness().recursiveRemove(
				"/plugins/" + plugin.getName());
		getDao().getPluginDao().remove(plugin.getId());
	}
	
	private void removePluginResources(PluginEntity plugin) {
		PluginResourceEntity listResource = getDao().getPluginResourceDao()
				.getByUrl(plugin.getName(), plugin.getName() + RESOURCE_LIST);
		if (listResource == null) {
			return;
		}
		List<Long> ids = new ArrayList<Long>();
		ids.add(listResource.getId());
		try {
			String list = new String(listResource.getContent(), "UTF-8");
			String[] resources = list.split(",");
			for (String id : resources) {
				ids.add(Long.valueOf(id));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		getDao().getPluginResourceDao().remove(ids);
	}

	private void removePluginFileCache(PluginEntity plugin) {
		PluginResourceEntity listResource = getDao().getPluginResourceDao()
			.getByUrl(plugin.getName(), plugin.getName() + FILE_LIST);
		if (listResource == null) {
			return;
		}
		getDao().getPluginResourceDao().remove(listResource.getId());
		try {
			String list = new String(listResource.getContent(), "UTF-8");
			String[] resources = list.split(",");
			for (String path : resources) {
				getBusiness().getSystemService().getFileCache().remove(path);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}	
	
	public Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}
	
}
