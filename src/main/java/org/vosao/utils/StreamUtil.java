package org.vosao.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

	public static byte[] readFileStream(final InputStream stream) throws IOException {
		byte[] buffer = new byte[4096];
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		long contentLength = 0;
		int n = 0;
		while (-1 != (n = stream.read(buffer))) {
			contentLength += n;
			output.write(buffer, 0, n);
		}
		return output.toByteArray();
	}

}
