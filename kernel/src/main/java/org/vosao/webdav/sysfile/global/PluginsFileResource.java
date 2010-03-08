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
import org.vosao.entity.PluginEntity;
import org.vosao.webdav.AbstractFileResource;

import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;

public class PluginsFileResource extends AbstractFileResource {

	public PluginsFileResource(Business aBusiness, String name) {
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
		Element e = doc.addElement("plugins");
		List<PluginEntity> list = getDao().getPluginDao().select();
		for (PluginEntity plugin : list) {
			createPluginXML(e, plugin);
		}
		setData(doc.asXML().getBytes("UTF-8"));
	}
	
	private void createPluginXML(Element pluginsElement, 
			final PluginEntity plugin) {
		Element pluginElement = pluginsElement.addElement("plugin");
		pluginElement.addElement("name").setText(plugin.getName());
		pluginElement.addElement("configData").setText(plugin.getConfigData());
	}

}
