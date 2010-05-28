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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.FileBusiness;
import org.vosao.business.FolderBusiness;
import org.vosao.business.FormBusiness;
import org.vosao.common.UploadException;
import org.vosao.dao.FormDao;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.FieldEntity;
import org.vosao.entity.FormConfigEntity;
import org.vosao.entity.FormDataEntity;
import org.vosao.entity.FormEntity;
import org.vosao.enums.FieldType;
import org.vosao.i18n.Messages;
import org.vosao.utils.EmailUtil;
import org.vosao.utils.FileItem;
import org.vosao.utils.FolderUtil;
import org.vosao.utils.ParamUtil;
import org.vosao.utils.StrUtil;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class FormBusinessImpl extends AbstractBusinessImpl 
	implements FormBusiness {

	private FolderBusiness folderBusiness;
	private FileBusiness fileBusiness;
	
	private FormDao getFormDao() {
		return getDao().getFormDao();
	}
	
	@Override
	public List<String> validateBeforeUpdate(final FormEntity entity) {
		List<String> errors = new ArrayList<String>();
		if (entity.getId() == null) {
			FormEntity myForm = getFormDao().getByName(entity.getName());
			if (myForm != null) {
				errors.add(Messages.get("form.already_exists"));
			}
		}
		if (StringUtils.isEmpty(entity.getName())) {
			errors.add(Messages.get("name_is_empty"));
		}
		if (StringUtils.isEmpty(entity.getTitle())) {
			errors.add(Messages.get("title_is_empty"));
		}
		if (StringUtils.isEmpty(entity.getEmail())) {
			errors.add(Messages.get("email_is_empty"));
		}
		return errors;
	}

	@Override
	public void submit(FormEntity form, Map<String, String> parameters,
			List<FileItem> files, String ipAddress) throws UploadException {
		filterXSS(parameters);
		FormDataEntity formData = saveFormData(form, parameters, files, 
				ipAddress);
		String error = sendEmail(formData);
		if (error != null) {
			throw new UploadException(error);
		}
	}
	
	private FormDataEntity saveFormData(FormEntity form, Map<String, String> parameters,
			List<FileItem> files, String ipAddress) {
		FormDataEntity formData = new FormDataEntity(form.getId(), "");
		formData.setIpAddress(ipAddress);
		getDao().getFormDataDao().save(formData);
		formData.setUuid(formData.getId().toString());
		Map<String, String> filesMap = saveFormDataFiles(formData, files);
		List<FieldEntity> fields = getDao().getFieldDao().getByForm(form);
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("formData");
		for (FieldEntity field: fields) {
			String value = parameters.containsKey(field.getName()) ? 
					parameters.get(field.getName()) : "";
			if (field.getFieldType().equals(FieldType.FILE) 
				&& filesMap.containsKey(field.getName())) {
				value = filesMap.get(field.getName());
			}
			root.addElement(field.getName()).setText(value);
		}
		formData.setData(doc.asXML());
		return getDao().getFormDataDao().save(formData);
	}
	
	private void filterXSS(Map<String, String> params) {
		for (String key : params.keySet()) {
			String value = params.get(key);
			params.put(key, ParamUtil.filterXSS(value));
		}
	}
	
	private Map<String, String> saveFormDataFiles(FormDataEntity formData,
			List<FileItem> files) {
		Map<String, String> result = new HashMap<String, String>();
		getFolderBusiness().createFolder(getFilePath(formData));
		for (FileItem file: files) {
			String filepath = getFilePath(formData) + "/" + file.getFilename();
			getFileBusiness().saveFile(filepath, file.getData());			
			result.put(file.getFieldName(), "/file" + filepath);
		}
		return result;
	}
	
	public String getFilePath(FormDataEntity formData) {
		FormEntity form = getFormDao().getById(formData.getFormId());
		return "/form/" + form.getName() + "/" + formData.getUuid();
	}

	public FolderBusiness getFolderBusiness() {
		return folderBusiness;
	}

	public void setFolderBusiness(FolderBusiness folderBusiness) {
		this.folderBusiness = folderBusiness;
	}

	public FileBusiness getFileBusiness() {
		return fileBusiness;
	}

	public void setFileBusiness(FileBusiness fileBusiness) {
		this.fileBusiness = fileBusiness;
	}

	@Override
	public String sendEmail(FormDataEntity formData) {
		FormEntity form = getFormDao().getById(formData.getFormId());
		ConfigEntity config = getDao().getConfigDao().getConfig();
		FormConfigEntity formConfig = getDao().getFormConfigDao().getConfig();
		VelocityContext context = new VelocityContext();
		List<FieldEntity> fields = getDao().getFieldDao().getByForm(form);
		context.put("form", form);
		context.put("fields", fields);
		context.put("values", formData.getValues());
		context.put("config", config);
		String letter = getSystemService().render(
				formConfig.getLetterTemplate(), context);
		List<String> emails = StrUtil.fromCSV(form.getEmail());
		for (String email : emails) {
			String error = EmailUtil.sendEmail(
					letter, 
					form.getLetterSubject(), 
					config.getSiteEmail(), 
					"Site admin", 
					StringUtils.strip(email), 
					getFileItems(formData));
			if (error != null) {
				return error;
			}
			logger.info("Form successfully submitted and emailed.");
		}
		return null;
	}
	
	private List<FileItem> getFileItems(FormDataEntity formData) {
		List<FileItem> result = new ArrayList<FileItem>();
		FormEntity form = getFormDao().getById(formData.getFormId());
		List<FieldEntity> fields = getDao().getFieldDao().getByForm(form);
		Map<String, String> values = formData.getValues();
		for (FieldEntity field : fields) {
			if (field.getFieldType().equals(FieldType.FILE)
				&& values.containsKey(field.getName()) 
				&& !StringUtils.isEmpty(values.get(field.getName()))) {
					String filepath = values.get(field.getName()).replace("/file", "");
					result.add(new FileItem(
							field.getName(), 
							FolderUtil.getFileName(filepath),
							getFileBusiness().readFile(filepath)));
			}
		}
		return result;
	}
}
