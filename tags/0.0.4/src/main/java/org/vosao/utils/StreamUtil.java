package org.vosao.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.core.io.ClassPathResource;

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

	public static String getTextResource(final String path) throws IOException {
		ClassPathResource res = new ClassPathResource(path);
		if (res == null) {
			throw new IOException("Empty resource.");
		}
		InputStream in = res.getInputStream();
		if (in == null) {
			throw new IOException("Empty input stream.");
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer buf = new StringBuffer();
		while(reader.ready()) {
			buf.append(reader.readLine()).append("\n");
		}
		return buf.toString();
	}
	
}
