package org.vosao.utils;

import org.dom4j.Element;

public class XmlUtil {

	public static boolean readBooleanAttr(Element e, String name, 
			boolean defaultValue) {
		String value = e.attributeValue(name);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Boolean.valueOf(value);
		}
		catch (Exception ex) {
			return defaultValue;
		}
	}
	
	public static int readIntAttr(Element e, String name, 
			int defaultValue) {
		String value = e.attributeValue(name);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Integer.valueOf(value);
		}
		catch (Exception ex) {
			return defaultValue;
		}
	}

	public static boolean readBooleanText(Element e, boolean defaultValue) {
		String value = e.getText();
		if (value == null) {
			return defaultValue;
		}
		try {
			return Boolean.valueOf(value);
		}
		catch (Exception ex) {
			return defaultValue;
		}
	}
	
	public static int readIntegerText(Element e, int defaultValue) {
		String value = e.getText();
		if (value == null) {
			return defaultValue;
		}
		try {
			return Integer.valueOf(value);
		}
		catch (Exception ex) {
			return defaultValue;
		}
	}

	public static Long readLongText(Element e, Long defaultValue) {
		String value = e.getText();
		if (value == null) {
			return defaultValue;
		}
		try {
			return Long.valueOf(value);
		}
		catch (Exception ex) {
			return defaultValue;
		}
	}

	
	
}
