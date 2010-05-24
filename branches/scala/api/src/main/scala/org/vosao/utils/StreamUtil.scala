package org.vosao.utils

import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

object StreamUtil {

	val logger = LogFactory.getLog(StreamUtil.getClass)
	
	def readFileStream(stream: InputStream): Array[Byte] = {
		val buffer = new Array[Byte](4096)
		val output = new ByteArrayOutputStream()
		var contentLength = 0L
		var n = stream.read(buffer)
		while (-1 != n) {
			contentLength += n
			output.write(buffer, 0, n)
			n = stream.read(buffer)
		}
		output.toByteArray
	}

	def getTextResource(path: String): String = { 
		getTextResource(StreamUtil.getClass.getClassLoader, path)
	}

	def getTextResource(classLoader: ClassLoader, path: String): String = {
		val in = classLoader.getResourceAsStream(path)
		if (in == null) {
			throw new IOException("Empty input stream.")
		}
		val reader = new BufferedReader(new InputStreamReader(in))
		val buf = new StringBuffer()
		while (reader.ready()) {
			buf.append(reader.readLine).append("\n")
		}
		buf.toString
	}

	/**
	 * Converts an object to an array of bytes . Uses the Logging utilities in
	 * j2sdk1.4 for reporting exceptions.
	 * 
	 * @param object
	 *            the object to convert.
	 * @return the associated byte array.
	 */
	def toBytes(obj: Any): Array[Byte] = {
		val baos = new ByteArrayOutputStream()
		try {
			val oos = new java.io.ObjectOutputStream(baos)
			oos.writeObject(obj)
		} catch {
			case ioe: IOException => logger.error(ioe.getMessage)
		}
		baos.toByteArray
	}

	/**
	 * Converts an array of bytes back to its constituent object. The input
	 * array is assumed to have been created from the original object. 
	 * 
	 * @param bytes
	 *            the byte array to convert.
	 * @return the associated object.
	 */
	def toObject(bytes: Array[Byte]): Any = {
		try {
			new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject()
		} catch {
			case ioe: IOException => logger.error(ioe.getMessage)
			case cnfe: ClassNotFoundException => logger.info(cnfe.getMessage)
		}
		null
	}
}
