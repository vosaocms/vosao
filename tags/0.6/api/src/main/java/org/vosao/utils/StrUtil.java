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

package org.vosao.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StrUtil {

	private static Log logger = LogFactory.getLog(StrUtil.class);

	public static final String DESCRIPTION_REGEX = 
		"<?(meta|META)\\s.*?((content|CONTENT)=\"(.*?)\"\\s+(name|NAME)=\"(description|DESCRIPTION)\"|(name|NAME)=\"(description|DESCRIPTION)\"\\s+(content|CONTENT)=\"(.*?)\")\\s*/>";
	public static final Pattern DESCRIPTION_PATTERN = Pattern.compile(DESCRIPTION_REGEX);	
	
	public static final String KEYWORDS_REGEX = 
		"<?(meta|META)\\s.*?((content|CONTENT)=\"(.*?)\"\\s+(name|NAME)=\"(keywords|KEYWORDS)\"|(name|NAME)=\"(keywords|KEYWORDS)\"\\s+(content|CONTENT)=\"(.*?)\")\\s*/>";
	public static final Pattern KEYWORDS_PATTERN = Pattern.compile(KEYWORDS_REGEX);	
	
	public static final String HEAD_CLOSE_REGEX = "</(head|HEAD)>";
	public static final Pattern HEAD_CLOSE_PATTERN = Pattern.compile(HEAD_CLOSE_REGEX);
	
	
	private static String _toCSV(Collection<String> list) {
		StringBuffer result = new StringBuffer();
		int count = 0;
		for (String item : list) {
			result.append((count == 0 ? "" : ",")).append(item);
			count++;
		}
		return result.toString();
	}

	public static String toCSV(Set<String> list) {
		return _toCSV(list);
	}

	public static String toCSV(List<String> list) {
		return _toCSV(list);
	}

	public static List<String> fromCSV(String data) {
		List<String> result = new ArrayList<String>();
		if (!StringUtils.isEmpty(data)) {
			if (data.indexOf(',') == -1) {
				result.add(data);
				return result;
			}
			for (String s : data.split(",")) {
				result.add(s);
			}
		}
		return result;
	}

	/**
	 * Gzip the input string into a byte[].
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static byte[] zipStringToBytes(String input) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BufferedOutputStream bufos = new BufferedOutputStream(
				new GZIPOutputStream(bos));
		bufos.write(input.getBytes("UTF-8"));
		bufos.close();
		byte[] retval = bos.toByteArray();
		bos.close();
		return retval;
	}

	/**
	 * Unzip a string out of the given gzipped byte array.
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	public static String unzipStringFromBytes(byte[] bytes) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		BufferedInputStream bufis = new BufferedInputStream(
				new GZIPInputStream(bis));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len;
		while ((len = bufis.read(buf)) > 0) {
			bos.write(buf, 0, len);
		}
		String retval = bos.toString("UTF-8");
		bis.close();
		bufis.close();
		bos.close();
		return retval;
	}

	public static String removeJavascript(String s) {
		StringBuffer buf = new StringBuffer(s);
		int scriptStart = buf.indexOf("<script");
		while (scriptStart > 0) {
			int scriptEnd = buf.indexOf("</script>", scriptStart) + 9;
			if (scriptEnd > 0) {
				buf.replace(scriptStart, scriptEnd, "");
			}
			scriptStart = buf.indexOf("<script", scriptStart);
		}
		return buf.toString();
	}
	
	private static final String[] VELOCITY_PATTERNS = {"\\$\\{.*\\}",
		"##.*$", 
		"#for\\s*\\(.*", 
		"#set\\s*\\(.*",
		"\\$\\w+\\.\\w+\\(.*\\)", 
		"\\$\\w+\\.\\w+",
		"#if\\s*\\(.*\\)", 
		"#end"};
	
	private static final String[] XML_PATTERNS = {"<\\!\\[CDATA\\[", "\\]\\]>"}; 
	private static final String[] HTML_PATTERNS = {"\\&gt;", "\\&lt;", "\\&nbsp;",
		"[\\W&&[^ ]]"}; 
	
	public static String extractSearchTextFromHTML(String html) {
		String result = removeJavascript(html).replaceAll("<.*?>", "");
		for (String pattern : VELOCITY_PATTERNS) {
			result = result.replaceAll(pattern, "");
		}
		for (String pattern : XML_PATTERNS) {
			result = result.replaceAll(pattern, "");
		}
		for (String pattern : HTML_PATTERNS) {
			result = result.replaceAll(pattern, " ");
		}
		return result;
	}

	public static String extractTextFromHTML(String html) {
		return removeJavascript(html).replaceAll("<.*?>", "");
	}

	public static List<Long> toLong(List<String> list) {
		List<Long> result = new ArrayList<Long>();
		for (String s : list) {
			try {
				result.add(Long.valueOf(s));
			}
			catch (NumberFormatException e) {
				logger.error("Wrong number format " + s);
			}
		}
		return result;
	}
	
	public static String[] splitByWord(String data) {
		return data.split("[ ,.:?!~#\n\t]+");
	}
	
}
