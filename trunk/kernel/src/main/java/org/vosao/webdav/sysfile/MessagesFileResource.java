package org.vosao.webdav.sysfile;

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
import org.vosao.entity.MessageEntity;
import org.vosao.webdav.AbstractFileResource;

import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;

public class MessagesFileResource extends AbstractFileResource {

	public MessagesFileResource(Business aBusiness) {
		super(aBusiness, "_messages.xml", new Date());
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
		Element e = doc.addElement("messages");
		List<MessageEntity> list = getDao().getMessageDao().select();
		for (MessageEntity message : list) {
			createMessageXML(e, message);
		}
		setData(doc.asXML().getBytes("UTF-8"));
	}

	private void createMessageXML(Element messagesElement, 
			final MessageEntity message) {
		Element messageElement = messagesElement.addElement("message");
		messageElement.addElement("language").setText(
				message.getLanguageCode());
		messageElement.addElement("code").setText(message.getCode());
		messageElement.addElement("value").setText(message.getValue());
	}
	
}
