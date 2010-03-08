package org.vosao.webdav.sysfile.local;

import static org.vosao.utils.XmlUtil.notNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.Business;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.FolderPermissionEntity;
import org.vosao.entity.GroupEntity;
import org.vosao.webdav.AbstractFileResource;

import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;

public class FolderFileResource extends AbstractFileResource {

	private FolderEntity folder;
	
	public FolderFileResource(Business aBusiness, FolderEntity aFolder) {
		super(aBusiness, "_folder.xml", new Date());
		setContentType("text/xml");
		setData(new byte[0]);
		folder = aFolder;
	}

	@Override
	public void sendContent(OutputStream out, Range range,
			Map<String, String> params, String aContentType) throws IOException,
			NotAuthorizedException, BadRequestException {
		createXML();
		super.sendContent(out, range, params, aContentType);
	}

	private void createXML() throws UnsupportedEncodingException {
		Document doc = DocumentHelper.createDocument();
		Element e = doc.addElement("folder");
		e.addElement("title").setText(notNull(folder.getTitle()));
		Element p = e.addElement("permissions");
		List<FolderPermissionEntity> list = getDao().getFolderPermissionDao()
				.selectByFolder(folder.getId());
		for (FolderPermissionEntity permission : list) {
			createFolderPermissionXML(p, permission);
		}
		setData(doc.asXML().getBytes("UTF-8"));
	}
	
	private void createFolderPermissionXML(Element permissionsElement, 
			final FolderPermissionEntity permission) {
		GroupEntity group = getDao().getGroupDao().getById(
				permission.getGroupId());
		Element permissionElement = permissionsElement.addElement("permission");
		permissionElement.addElement("group").setText(group.getName());
		permissionElement.addElement("permissionType").setText(
				permission.getPermission().name());
	}

}
