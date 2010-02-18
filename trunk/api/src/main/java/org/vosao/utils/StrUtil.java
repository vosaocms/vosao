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

public class StrUtil {

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
		for (String s : data.split(",")) {
			result.add(s);
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
	
	public static String extractTextFromHTML(String html) {
		return removeJavascript(html).replaceAll("<.*?>", "");
	}
	
}
