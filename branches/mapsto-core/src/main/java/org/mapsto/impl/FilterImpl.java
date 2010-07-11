package org.mapsto.impl;

import java.util.Date;

import org.apache.commons.beanutils.BeanMap;
import org.mapsto.Filter;

public class FilterImpl implements Filter<Object> {

	public String field;
	public int operator;
	public Object parameter;
	
	public FilterImpl(String field, int oper, Object param) {
		this.field = field;
		operator = oper;
		parameter = param;
	}
	
	public boolean check(Object entity) {
		BeanMap map = new BeanMap(entity);
		Object value = map.get(field);
		if (parameter == null) {
			return value == null;
		}
		else {
			if (value == null) {
				return false;
			}
			if (!parameter.getClass().equals(value.getClass())) {
				return false;
			}
			if (parameter instanceof Integer) {
				Integer param = (Integer) parameter;
				Integer val = (Integer) value;
				if (operator == EQUAL) {
					return param.equals(val);
				}
				if (operator == MORE) {
					return param < val;
				}
				if (operator == LESS) {
					return param > val;
				}
				if (operator == MORE_EQUAL) {
					return param <= val;
				}
				if (operator == LESS_EQUAL) {
					return param >= val;
				}
			}
			if (parameter instanceof Long) {
				Long param = (Long) parameter;
				Long val = (Long) value;
				if (operator == EQUAL) {
					return param.equals(val);
				}
				if (operator == MORE) {
					return param < val;
				}
				if (operator == LESS) {
					return param > val;
				}
				if (operator == MORE_EQUAL) {
					return param <= val;
				}
				if (operator == LESS_EQUAL) {
					return param >= val;
				}
			}
			if (parameter instanceof String) {
				String param = (String) parameter;
				String val = (String) value;
				if (operator == EQUAL) {
					return param.equals(val);
				}
				if (operator == MORE) {
					return param.compareTo(val) < 0;
				}
				if (operator == LESS) {
					return param.compareTo(val) > 0;
				}
				if (operator == MORE_EQUAL) {
					return param.compareTo(val) <= 0;
				}
				if (operator == LESS_EQUAL) {
					return param.compareTo(val) >= 0;
				}
			}
			if (parameter instanceof Date) {
				Date param = (Date) parameter;
				Date val = (Date) value;
				if (operator == EQUAL) {
					return param.equals(val);
				}
				if (operator == MORE) {
					return param.compareTo(val) < 0;
				}
				if (operator == LESS) {
					return param.compareTo(val) > 0;
				}
				if (operator == MORE_EQUAL) {
					return param.compareTo(val) <= 0;
				}
				if (operator == LESS_EQUAL) {
					return param.compareTo(val) >= 0;
				}
			}
			if (operator == EQUAL) {
				return parameter.equals(value);
			}
		}
		return false;
	}
	
}
