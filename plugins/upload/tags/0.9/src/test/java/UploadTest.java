/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import junit.framework.TestCase;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.io.FileUtils;

public class UploadTest extends TestCase {

	private static final String URL = "http://localhost:9000/_ah/plugin/upload/post";
	private static final String KEY = "secret";
	
	private static void log(String msg) {
		System.out.println(msg);
	}
	
	private static String generateChallenge() {
		return UUID.randomUUID().toString();
	}

	private static String generateVerify(String challenge, String key, 
			byte[] data) {
		return DigestUtils.shaHex(challenge + key + DigestUtils.shaHex(data));
	}
	
	private static String sendRequest(String challenge) throws HttpException, 
			IOException {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(URL);
		method.setParameter("filename", "test1.txt");
		String textFile = "Hello World!";
		method.setParameter("data", textFile);
		method.setParameter("challenge", challenge);
		
		method.setParameter("verify", generateVerify(challenge, KEY, 
				textFile.getBytes("UTF-8")));

		Integer status = client.executeMethod(method);
		String response = method.getResponseBodyAsString();
		method.releaseConnection();
		return status + " " + response;
	}
	
	private static String sendMultipartRequest(String challenge) 
			throws HttpException, IOException {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(URL);
		File file = new File("/home/oleynik/images/soprano.jpg");
		byte[] data = FileUtils.readFileToByteArray(file);
		Part[] parts = { 
				new StringPart("filename","soprano.jpg"),
				new StringPart("challenge",challenge),
				new StringPart("verify", generateVerify(challenge, KEY, data)),
				new FilePart("data", file)
		};
		method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));
		client.executeMethod(method);
		Integer status = client.executeMethod(method);
		String response = method.getResponseBodyAsString();
		method.releaseConnection();
		return status + " " + response;
	}
	
	public void testOK() {
	}
	
	public void mtestPost() throws HttpException, IOException {
		log("Start upload test: " + URL);
		String challenge = generateChallenge();
		log("challenge: " + challenge);
		log("Result of request 1: " + sendRequest(challenge));
		log("Result of request 2: " + sendMultipartRequest(challenge));
	}
	
}
