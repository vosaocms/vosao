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
import org.vosao.entity.ContentPermissionEntity;
import org.vosao.entity.GroupEntity;
import org.vosao.webdav.AbstractFileResource;

import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;

public class PagePermissionsFileResource extends AbstractFileResource {

	private String pageURL;
	
	public PagePermissionsFileResource(Business aBusiness, String aPageURL) {
		super(aBusiness, "_page_permissions.xml", new Date());
		setContentType("text/xml");
		setData(new byte[0]);
		pageURL = aPageURL;
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
		Element e = doc.addElement("permissions");
		List<ContentPermissionEntity> list = getDao().getContentPermissionDao()
				.selectByUrl(pageURL);
		for (ContentPermissionEntity permission : list) {
			createPagePermissionXML(e, permission);
		}
		setData(doc.asXML().getBytes("UTF-8"));
	}

	private void createPagePermissionXML(Element permissionsElement, 
			final ContentPermissionEntity permission) {
		GroupEntity group = getDao().getGroupDao().getById(permission.getGroupId());
		Element permissionElement = permissionsElement.addElement("permission");
		permissionElement.addElement("group").setText(group.getName());
		permissionElement.addElement("permissionType").setText(
				permission.getPermission().name());
		permissionElement.addElement("allLanguages").setText(
				String.valueOf(permission.isAllLanguages()));
		permissionElement.addElement("languages").setText(notNull(
				permission.getLanguages()));
	}

}
