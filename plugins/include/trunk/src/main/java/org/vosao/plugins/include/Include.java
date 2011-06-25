package org.vosao.plugins.include;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;

import org.apache.commons.io.IOUtils;

public class Include
{
	private final String content;
	private final Date time = new Date();
	
	public Include(final String content)
	{
		this.content = content;
	}
	
	public String getContent()
	{
		return content;
	}
	
	public Date getFetchTime()
	{
		return time;
	}
	
	public static Include load(final URL url, final String lang)
		throws IOException
	{
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setUseCaches(false);
		if (lang != null)
			con.setRequestProperty("Accept-Language", lang);

		if (con.getResponseCode() != HttpURLConnection.HTTP_OK)
			throw new IOException(String.format("Bad server response: %d (%s)", con.getResponseCode(), con.getResponseMessage()));
		
		Charset charset = Utils.extractCharsetFromContentType(con.getContentType());
		if (charset == null)
			return load(con.getInputStream());
		else
			return load(new InputStreamReader(con.getInputStream(), charset));
	}
	
	public static Include load(final byte[] bytes)
	{
		int offset = 0;

		if (new String(bytes, 0, 5).equals("<?xml"))
		{
			int p = Utils.findFirst(bytes, "?>".getBytes());
			if (p != -1)
			{
				p += 2;
				Charset charset = Utils.extractCharsetFromXMLPreamble(new String(bytes, 0, p));
				offset = p;
				if (charset != null)
					return new Include(new String(bytes, offset, bytes.length-offset, charset));
			}
		}

		return new Include(new String(bytes, offset, bytes.length-offset));
	}
	
	public static Include load(final InputStream is)
		throws IOException
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		IOUtils.copyLarge(is, os);
		return load(os.toByteArray());
	}
	
	public static Include load(final Reader reader)
		throws IOException
	{
		StringWriter content = new StringWriter();
		IOUtils.copyLarge(reader, content);
		return new Include(content.toString());
	}
}
