package org.vosao.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

public class StreamUtil {

	private static final Log logger = LogFactory.getLog(
			StreamUtil.class);

	
	public static byte[] readFileStream(final InputStream stream)
			throws IOException {
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
		return getTextResource(StreamUtil.class.getClassLoader(), path);
	}

	public static String getTextResource(ClassLoader classLoader,
			final String path) throws IOException {
		ClassPathResource res = new ClassPathResource(path, classLoader);
		if (res == null) {
			throw new IOException("Empty resource.");
		}
		InputStream in = res.getInputStream();
		if (in == null) {
			throw new IOException("Empty input stream.");
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer buf = new StringBuffer();
		while (reader.ready()) {
			buf.append(reader.readLine()).append("\n");
		}
		return buf.toString();
	}

	/**
	 * Converts an object to an array of bytes . Uses the Logging utilities in
	 * j2sdk1.4 for reporting exceptions.
	 * 
	 * @param object
	 *            the object to convert.
	 * @return the associated byte array.
	 */
	public static byte[] toBytes(Object object) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
			oos.writeObject(object);
		} catch(IOException ioe) {
			logger.error(ioe.getMessage()); 
		}
		return baos.toByteArray();
	}

	/**
	 * Converts an array of bytes back to its constituent object. The input
	 * array is assumed to have been created from the original object. 
	 * 
	 * @param bytes
	 *            the byte array to convert.
	 * @return the associated object.
	 */
	public static Object toObject(byte[] bytes) {
		Object object = null;
		try {
			object = new ObjectInputStream(new ByteArrayInputStream(bytes))
					.readObject();
		} catch (IOException ioe) {
			logger.error(ioe.getMessage());
		} catch (ClassNotFoundException cnfe) {
			logger.info(cnfe.getMessage());
		}
		return object;
	}
}
