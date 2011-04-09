package org.vosao.plugins.include;

import java.nio.charset.Charset;

public class Utils
{
	protected static int findFirst(byte[] haystack, byte[] needle)
	{
		for (int i=0 ; i<haystack.length ; i++)
		{
			boolean match = true;
			for (int c=0 ; c<needle.length ; c++)
			{
				if (haystack[i+c] != needle[c])
				{
					match = false;
					break;
				}
			}
			
			if (match)
				return i;
		}
		
		return -1;
	}
	
	protected static Charset extractCharsetFromContentType(String content_type)
	{
		final String ptn = "charset=";
		
		if (content_type == null)
			return null;
		
		int p0 = content_type.indexOf(ptn);
		if (p0 == -1)
			return null;
		
		p0 += ptn.length();
		int p1 = content_type.indexOf(';', p0);
		if (p1 == -1)
			return Charset.forName(content_type.substring(p0));
		else
			return Charset.forName(content_type.substring(p0, p1));
	}

	protected static Charset extractCharsetFromXMLPreamble(final String preamble)
	{
		final String ptn = "encoding=\"";
		
		int p0 = preamble.indexOf(ptn);
		if (p0 == -1)
			return null;

		p0 += ptn.length();
		int p1 = preamble.indexOf('"', p0);
		if (p1 == -1)
			return null;

		return Charset.forName(preamble.substring(p0, p1));
	}
	
}
