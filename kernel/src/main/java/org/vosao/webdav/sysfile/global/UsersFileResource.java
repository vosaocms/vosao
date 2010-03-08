package org.vosao.webdav.sysfile.global;

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
import org.vosao.entity.UserEntity;
import org.vosao.webdav.AbstractFileResource;

import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;

public class UsersFileResource extends AbstractFileResource {

	public UsersFileResource(Business aBusiness, String name) {
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
		Element e = doc.addElement("users");
		List<UserEntity> list = getDao().getUserDao().select();
		for (UserEntity user : list) {
			createUserXML(e, user);
		}
		setData(doc.asXML().getBytes("UTF-8"));
	}
	
	private void createUserXML(Element usersElement, final UserEntity user) {
		Element userElement = usersElement.addElement("user");
		userElement.addElement("name").setText(user.getName());
		userElement.addElement("email").setText(user.getEmail());
		userElement.addElement("password").setText(notNull(user.getPassword()));
		userElement.addElement("role").setText(user.getRole().name());
	}

}
