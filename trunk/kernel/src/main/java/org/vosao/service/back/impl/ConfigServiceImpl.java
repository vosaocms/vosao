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

package org.vosao.service.back.impl;

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.url;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.vosao.business.SetupBean;
import org.vosao.business.impl.SetupBeanImpl;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.FileEntity;
import org.vosao.i18n.Messages;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.ConfigService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.servlet.ExportTaskServlet;
import org.vosao.servlet.SessionCleanTaskServlet;
import org.vosao.utils.StrUtil;
import org.vosao.utils.StreamUtil;

import com.google.appengine.api.labs.taskqueue.Queue;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class ConfigServiceImpl extends AbstractServiceImpl 
		implements ConfigService {

	private SetupBean setupBean;
	
	@Override
	public ConfigEntity getConfig() {
		return getBusiness().getConfigBusiness().getConfig();
	}

	@Override
	public ServiceResponse saveConfig(Map<String, String> vo) {
		ConfigEntity config = getBusiness().getConfigBusiness().getConfig();
		if (vo.get("commentsEmail") != null) {
			config.setCommentsEmail(vo.get("commentsEmail"));
		}
		if (vo.get("commentsTemplate") != null) {
			config.setCommentsTemplate(vo.get("commentsTemplate"));
		}
		if (vo.get("editExt") != null) {
			config.setEditExt(vo.get("editExt"));
		}
		if (vo.get("googleAnalyticsId") != null) {
			config.setGoogleAnalyticsId(vo.get("googleAnalyticsId"));
		}
		if (vo.get("enableRecaptcha") != null) {
			config.setEnableRecaptcha(Boolean.valueOf(vo.get("enableRecaptcha")));
		}
		if (vo.get("recaptchaPrivateKey") != null) {
			config.setRecaptchaPrivateKey(vo.get("recaptchaPrivateKey"));
		}
		if (vo.get("recaptchaPublicKey") != null) {
			config.setRecaptchaPublicKey(vo.get("recaptchaPublicKey"));
		}
		if (vo.get("siteDomain") != null) {
			config.setSiteDomain(vo.get("siteDomain"));
		}
		if (vo.get("siteEmail") != null) {
			config.setSiteEmail(vo.get("siteEmail"));
		}
		if (vo.get("siteUserLoginUrl") != null) {
			config.setSiteUserLoginUrl(vo.get("siteUserLoginUrl"));
		}
		if (vo.get("enablePicasa") != null) {
			config.setEnablePicasa(Boolean.valueOf(vo.get("enablePicasa")));
		}
		if (vo.get("picasaUser") != null) {
			config.setPicasaUser(vo.get("picasaUser"));
		}
		if (vo.get("picasaPassword") != null) {
			config.setPicasaPassword(vo.get("picasaPassword"));
		}
		List<String> errors = getBusiness().getConfigBusiness()
				.validateBeforeUpdate(config);
		if (errors.isEmpty()) {
			getDao().getConfigDao().save(config);
			return ServiceResponse.createSuccessResponse(
					Messages.get("successfull_save", "Configuration"));
		}
		else {
			return ServiceResponse.createErrorResponse(
					Messages.get("error_during_save", "config"), errors);
		}
	}

	@Override
	public ServiceResponse restoreCommentsTemplate() throws IOException {
		ConfigEntity config = getDao().getConfigDao().getConfig();
		config.setCommentsTemplate(StreamUtil.getTextResource(
			SetupBeanImpl.COMMENTS_TEMPLATE_FILE));
		getDao().getConfigDao().save(config);			
		return ServiceResponse.createSuccessResponse(
				Messages.get("successfull_save", "Comments template"));
	}

	@Override
	public ServiceResponse reset() {
		getSetupBean().clear();
		getSetupBean().clearFileCache();
		getSetupBean().setup();
		return ServiceResponse.createSuccessResponse(
				Messages.get("successfull_reset", "Site"));
	}

	public SetupBean getSetupBean() {
		return setupBean;
	}

	public void setSetupBean(SetupBean setupBean) {
		this.setupBean = setupBean;
	}

	@Override
	public ServiceResponse reindex() {
		getBusiness().getSearchEngine().reindex();
		return ServiceResponse.createSuccessResponse(
				Messages.get("index_creation_started"));
	}

	@Override
	public ServiceResponse cacheReset() {
		getDao().clearCache();
		getSetupBean().clearFileCache();
		Queue queue = getBusiness().getSystemService().getQueue("session-clean");
		queue.add(url(SessionCleanTaskServlet.SESSION_CLEAN_TASK_URL));
		return ServiceResponse.createSuccessResponse(
				Messages.get("successfull_reset", "Cache"));
	}

	@Override
	public ServiceResponse startExportTask(String exportType) {
		String filename = ExportTaskServlet.getExportFilename(exportType);
		if (filename != null) {
			Queue queue = getBusiness().getSystemService().getQueue("export");
			queue.add(url(ExportTaskServlet.EXPORT_TASK_URL)
				.param("filename", filename)
				.param("exportType", exportType));
			return ServiceResponse.createSuccessResponse(filename);
		}
		return ServiceResponse.createErrorResponse(
				Messages.get("unknown_export_type", exportType));
	}

	@Override
	public boolean isExportTaskFinished(String exportType) {
		String finishFilename = "/tmp/" + ExportTaskServlet.getExportFilename(
				exportType) + ".txt";
		FileEntity file = getBusiness().getFileBusiness().findFile(
				finishFilename);
		if (file != null) {
			String content = new String(getDao().getFileDao().getFileContent(
					file));
			if ("OK".equals(content)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public ServiceResponse startExportThemeTask(List<String> ids) {
		String filename = ExportTaskServlet.getExportFilename(
				ExportTaskServlet.TYPE_PARAM_THEME);
		Queue queue = getBusiness().getSystemService().getQueue("export");
		queue.add(url(ExportTaskServlet.EXPORT_TASK_URL)
			.param("filename", filename)
			.param("exportType", ExportTaskServlet.TYPE_PARAM_THEME)
			.param("ids", StrUtil.toCSV(ids)));
		return ServiceResponse.createSuccessResponse(filename);
	}

	@Override
	public ServiceResponse startExportFolderTask(Long folderId) {
		String filename = ExportTaskServlet.getExportFilename(
				ExportTaskServlet.TYPE_PARAM_FOLDER);
		Queue queue = getBusiness().getSystemService().getQueue("export");
		queue.add(url(ExportTaskServlet.EXPORT_TASK_URL)
			.param("filename", filename)
			.param("exportType", ExportTaskServlet.TYPE_PARAM_FOLDER)
			.param("folderId", folderId.toString()));
		return ServiceResponse.createSuccessResponse(filename);
	}
	
}
