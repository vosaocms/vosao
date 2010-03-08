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
import org.vosao.entity.LanguageEntity;
import org.vosao.webdav.AbstractFileResource;

import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;

public class LanguagesFileResource extends AbstractFileResource {

	public LanguagesFileResource(Business aBusiness) {
		super(aBusiness, "_languages.xml", new Date());
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
		Element e = doc.addElement("languages");
		List<LanguageEntity> langs = getDao().getLanguageDao().select();
		for (LanguageEntity lang : langs) {
			Element langElem = e.addElement("language");
			langElem.addAttribute("code", lang.getCode());
			langElem.addAttribute("title", lang.getTitle());
		}
		setData(doc.asXML().getBytes("UTF-8"));
	}
	
}
