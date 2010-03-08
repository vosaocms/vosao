package org.vosao.webdav.sysfile.global;

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
import org.vosao.entity.GroupEntity;
import org.vosao.entity.UserEntity;
import org.vosao.entity.UserGroupEntity;
import org.vosao.entity.helper.UserHelper;
import org.vosao.webdav.AbstractFileResource;

import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;

public class GroupsFileResource extends AbstractFileResource {

	public GroupsFileResource(Business aBusiness, String name) {
		super(aBusiness, name, new Date());
		setContentType("text/xml");
		setData(new byte[0]);
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
		Element e = doc.addElement("groups");
		List<GroupEntity> list = getDao().getGroupDao().select();
		for (GroupEntity Group : list) {
			createGroupXML(e, Group);
		}
		setData(doc.asXML().getBytes("UTF-8"));
	}
	
	private void createGroupXML(Element groupsElement, final GroupEntity group) {
		Element groupElement = groupsElement.addElement("group");
		groupElement.addElement("name").setText(group.getName());
		List<UserGroupEntity> userGroups = getDao().getUserGroupDao()
				.selectByGroup(group.getId());
		Element users = groupElement.addElement("users");
		Map<Long, UserEntity> usersMap = UserHelper.createIdMap(getDao()
						.getUserDao().select()); 
		for (UserGroupEntity userGroup : userGroups) {
			UserEntity user = usersMap.get(userGroup.getUserId());
			if (user != null) {
				users.addElement("user").setText(user.getEmail());
			}
		}
	}
	
}
