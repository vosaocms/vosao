package org.vosao.webdav;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import org.vosao.business.Business;

import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.GetableResource;
import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;

public class AbstractFileResource extends AbstractResource 
		implements GetableResource {

	private byte[] data;
	private String contentType;
	
	public AbstractFileResource(Business aBusiness, String aName, 
			Date aModDate) {
		super(aBusiness, aName, aModDate);
	}

	@Override
	public Long getContentLength() {
		return new Long(data.length);
	}

	@Override
	public String getContentType(String arg0) {
		return contentType;
	}

	@Override
	public Long getMaxAgeSeconds(Auth arg0) {
		return 10L;
	}

	@Override
	public void sendContent(OutputStream out, Range range,
			Map<String, String> params, String aContentType) throws IOException,
			NotAuthorizedException, BadRequestException {
		int start = 0;
		int finish = data.length;
		if (range != null) {
			start = ((Long)range.getStart()).intValue();
			finish = ((Long)range.getFinish()).intValue();
		}
		out.write(data, start, finish - start);
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
