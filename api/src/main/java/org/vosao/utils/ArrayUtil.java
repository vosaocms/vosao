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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtil {

	public static List<byte[]> makeChunks(byte[] data, int chunkSize) {
		List<byte[]> result = new ArrayList<byte[]>();
		long n = data.length / chunkSize;
		int finalChunkSize = data.length % chunkSize;
		int i = 0;
		int start;
		int end;
		for (i = 0; i < n; i++) {
			start = i * chunkSize;
			end = start + chunkSize;
			result.add(Arrays.copyOfRange(data, start, end));
		}
		if (finalChunkSize > 0) {
			start = i * chunkSize;
			end = start + finalChunkSize;
			result.add(Arrays.copyOfRange(data, start, end));
		}
		return result;
	}
	
	public static byte[] packChunks(List<byte[]> data) {
		int size = 0;
		for (byte[] chunk : data) {
			size += chunk.length;
		}
		byte[] result = new byte[size];
		int start = 0;
		for (byte[] chunk : data) {
			System.arraycopy(chunk, 0, result, start, chunk.length);
			start += chunk.length;
		}
		return result;
	}
	
}
