package org.vosao.common;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import junit.framework.TestCase;

public class JSONTest extends TestCase {
	
	public void testJSON() throws JSONException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ru", "Privet");
		map.put("en", "Hello");
		JSONObject obj = new JSONObject(map);
		JSONObject obj2 = new JSONObject(obj.toString());
		assertEquals("Hello", obj2.getString("en"));
	}

}
