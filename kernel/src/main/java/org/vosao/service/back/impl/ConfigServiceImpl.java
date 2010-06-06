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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.vosao.business.SetupBean;
import org.vosao.business.impl.SetupBeanImpl;
import org.vosao.business.impl.mq.message.ExportMessage;
import org.vosao.business.impl.mq.subscriber.ExportTaskSubscriber;
import org.vosao.business.mq.Topic;
import org.vosao.business.mq.message.SimpleMessage;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.FileEntity;
import org.vosao.i18n.Messages;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.ConfigService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.utils.StrUtil;
import org.vosao.utils.StreamUtil;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class ConfigServiceImpl extends AbstractServiceImpl 
		implements ConfigService {

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

	private SetupBean getSetupBean() {
		return getBusiness().getSetupBean();
	}

	@Override
	public ServiceResponse reindex() {
		getBusiness().getSearchEngine().reindex();
		return ServiceResponse.createSuccessResponse(
				Messages.get("index_creation_started"));
	}

	@Override
	public ServiceResponse cacheReset() {
		getBusiness().getSystemService().getCache().clear();
		getSetupBean().clearFileCache();
		getMessageQueue().publish(new SimpleMessage(
				Topic.SESSION_CLEAN.name(), "start"));
		return ServiceResponse.createSuccessResponse(
				Messages.get("successfull_reset", "Cache"));
	}

	@Override
	public ServiceResponse startExportTask(String exportType) {
		try {
			
		String filename = ExportTaskSubscriber.getExportFilename(exportType);
		if (filename != null) {
			getMessageQueue().publish(new ExportMessage.Builder()
					.setFilename(filename)
					.setExportType(exportType).create());
			return ServiceResponse.createSuccessResponse(filename);
		}
		return ServiceResponse.createErrorResponse(
				Messages.get("unknown_export_type", exportType));
		
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean isExportTaskFinished(String exportType) {
		String finishFilename = "/tmp/" + ExportTaskSubscriber
				.getExportFilename(exportType) + ".txt";
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
		String filename = ExportTaskSubscriber.getExportFilename(
				ExportTaskSubscriber.TYPE_PARAM_THEME);
		getMessageQueue().publish(new ExportMessage.Builder()
				.setFilename(filename)
				.setExportType(ExportTaskSubscriber.TYPE_PARAM_THEME)
				.setIds(StrUtil.toLong(ids)).create());
		return ServiceResponse.createSuccessResponse(filename);
	}

	@Override
	public ServiceResponse startExportFolderTask(Long folderId) {
		String filename = ExportTaskSubscriber.getExportFilename(
				ExportTaskSubscriber.TYPE_PARAM_FOLDER);
		getMessageQueue().publish(new ExportMessage.Builder()
				.setFilename(filename)
				.setExportType(ExportTaskSubscriber.TYPE_PARAM_FOLDER)
				.setFolderId(folderId).create());
		return ServiceResponse.createSuccessResponse(filename);
	}
	
}
