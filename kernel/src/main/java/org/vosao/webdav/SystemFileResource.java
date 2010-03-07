package org.vosao.webdav;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.vosao.business.Business;

public class SystemFileResource extends AbstractFileResource {

	public SystemFileResource(Business aBusiness, String aName, 
			Date aModDate, String content) {
		super(aBusiness, aName, aModDate);
		try {
			setData(content.getBytes("UTF-8"));
		}
		catch(UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
	}

}
