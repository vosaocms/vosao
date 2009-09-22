package org.vosao.jsf;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.servlet.FolderUtil;


public class FileBean extends AbstractJSFBean implements Serializable {

	private static final long serialVersionUID = 3L;
	private static Log log = LogFactory.getLog(FileBean.class);
	
	private FileEntity current;
	private FolderEntity folder;
	private String id;
	private String content;

	public void init() {
		initCurrent();
	}
	
	private void initCurrent() {
		if (getCurrentId() != null) {
			current = getDao().getFileDao().getById(getCurrentId());
			if (isEditContent()) {
				try {
					content = new String(getDao().getFileDao()
							.getFileContent(current), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					JSFUtil.addErrorMessage("Encoding error while getting file content");
				}
			}
			folder = getDao().getFolderDao().getById(current.getFolderId());
		}
		else {
			current = new FileEntity();
		}
	}
	
	public String cancelEdit() {
		return "pretty:folders";
	}
	
	public void update() {
		if (current.getId() != null) {
			List<String> errors = getBusiness().getFileBusiness()
				.validateBeforeUpdate(current);
			if (errors.isEmpty()) {
				getDao().getFileDao().save(current);
				JSFUtil.addInfoMessage("File was successfully saved.");
			}
			else {
				JSFUtil.addErrorMessages(errors);
			}
		}
	}
	
	public void updateContent() {
		byte[] data = content.getBytes(); 
		current.setMdtime(new Date());
		current.setSize(data.length);
		getDao().getFileDao().save(current);
		getDao().getFileDao().saveFileContent(current, data);
		JSFUtil.addInfoMessage("Content was successfully saved.");
	}
	
	public void edit() {
		if (id != null) {
			setCurrentId(id);
			initCurrent();
		}
	}
	
	public FileEntity getCurrent() {
		return current;
	}

	public void setCurrent(FileEntity current) {
		this.current = current;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCurrentId() {
		String name = this.getClass().getName() + "currentId";
		return (String)JSFUtil.getSessionObject(name);
	}

	public void setCurrentId(String data) {
		String name = this.getClass().getName() + "currentId";
		JSFUtil.setSessionObject(name, data);
	}
	
	public boolean isEditContent() {
		String editExt = getBusiness().getConfigBusiness().getEditExt();
		String[] exts = editExt.split(",");
		for (String ext : exts) {
			if (FolderUtil.getFileExt(current.getFilename()).equals(ext)) {
				return true;
			}
		}
		return false;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	private static final String[] IMAGE_EXTENSIONS = {"jpg","jpeg","png","ico",
		"gif"};
	
	public boolean isImageContent() {
		for (String ext : IMAGE_EXTENSIONS) {
			if (FolderUtil.getFileExt(current.getFilename()).equals(ext)) {
				return true;
			}
		}
		return false;
	}
	
	public String getFileLink() {
		if (folder != null) {
			return "/file" + getBusiness().getFolderBusiness()
				.getFolderPath(folder) + "/" + current.getFilename();
		}
		return "";
	}
	
}
