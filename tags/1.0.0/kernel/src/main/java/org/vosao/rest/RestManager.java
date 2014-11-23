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

package org.vosao.rest;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.vosao.common.VosaoContext;


/**
 * RestFilter is mapped to /rest
 * 
 * MyRest mapped to "my"
 * MyRest.apply() -> /rest/my
 * MyRest.apply(a,b) -> /rest/my/a/b, /rest/my?a=xxx&b=yyy
 * MyRest.open(a,b) -> /rest/my/open/a/b, /rest/my/open?a=xxx&b=yyy 
 **/
public class RestManager {

	private static class MatchResult {
		public Method method;
		public Object[] args;
		
		public MatchResult(Method method, Object[] args) {
			super();
			this.method = method;
			this.args = args;
		}
	}
	
	private static final Log logger = LogFactory.getLog(RestManager.class);
	
	private Map<String, Object> services;
	
	public RestManager() {
		services = new HashMap<String, Object>();
	}
	
	public void addService(String name, Object service) {
		services.put(name, service);
	}
	
	@SuppressWarnings("unchecked")
	public String execute(HttpServletRequest request) throws Throwable {
		List<String> parts = Arrays.asList(request.getRequestURI().split("/"));
		if (parts.size() <= 2) return null;
		String restKey = parts.get(2);
		parts = parts.subList(3, parts.size());
		return runService(restKey, parts, toStringMap(request.getParameterMap()));
	}
	
	private static Map<String, String> toStringMap(Map<String, String[]> params) {
		Map<String, String> result = new HashMap<String, String>();
		for (String key : params.keySet()) result.put(key, params.get(key)[0]);
		return result;
	}
	
	private String runService(String serviceKey, List<String> path, 
			Map<String, String> params) throws Throwable {
		Object restService = services.get(serviceKey);
		if (restService != null) {
			// check Authorized
			if (restService.getClass().getAnnotation(Authorized.class) != null) {
				if (VosaoContext.getInstance().getUser() == null) {
					throw new AuthenticationException("User is not authenticated.");
				}
			}
			MatchResult result = matchMethod(restService, path, params);
			if (result != null) {
				try {
					// check Authorized
					if (result.method.getAnnotation(Authorized.class) != null) {
						if (VosaoContext.getInstance().getUser() == null) {
							throw new AuthenticationException("User is not authenticated.");
						}
					}
					Object invokeResult = result.method.invoke(restService, result.args);
					if (invokeResult instanceof String) return (String)invokeResult;
					else return new JSONObject(invokeResult).toString();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					if (e.getCause() != null) throw e.getCause();
					e.printStackTrace();
				}
			}
		}
		return null;		
	}

	private MatchResult matchMethod(Object restService, List<String> path, 
			Map<String, String> queryParams) {
		MatchResult result = null;
		
		// match service methods
		if (path.size() > 0) {
			String methodName = path.get(0);

			// 1. No Var annotation
			// /method/a/b  ->   service.method(a,b)
			List<String> pathParams = Collections.EMPTY_LIST;
			if (path.size() > 1) pathParams = path.subList(1, path.size());
			result = matchPathMethod(restService, methodName, pathParams);
			if (result != null) return result;
			
			// 2. With Var annotation
			// /method?a=xxx&b=yyy  ->  service.method(a,b)
			if (path.size() == 1) {
				result = matchQueryMethod(restService, methodName, queryParams);
				if (result != null) return result;
			}
		}
		
		// match service apply method
		// 1. With Var annotation
		// ?a=xxx&b=yyy  ->  service.apply(a,b)
		result = matchQueryMethod(restService, "apply", queryParams);
		if (result != null) return result;

		// 2. No Var annotation
		// /a/b  ->  service.apply(a,b)
		result = matchPathMethod(restService, "apply", path);
		return result;
	}

	private MatchResult matchQueryMethod(Object restService, String methodName,
			Map<String, String> queryParams) {
		MatchResult result = null;
		for (Method m : restService.getClass().getMethods()) {
			if (!m.getName().equals(methodName)) continue;
			if (m.isVarArgs()) 
				throw new IllegalArgumentException("Varargs are not supported");
				
			Class<?>[] paramTypes = m.getParameterTypes();
			if (paramTypes.length != queryParams.size()) continue;
			List<String> paramsList = new ArrayList<String>();
			for (int i = 0; i < paramTypes.length; i++) {
				String paramName = getParamNameFromArgAnnotation(m.getParameterAnnotations()[i]);
				if (paramName != null && queryParams.containsKey(paramName))  {
					paramsList.add(queryParams.get(paramName));
				}
			}
			if (paramsList.size() != queryParams.size()) continue;
			Object[] convertedArgs = null;
			try {
				convertedArgs = convertArgs(paramTypes, paramsList);
			}
			catch (Exception e) {
			}
			if (convertedArgs == null) continue;
			return new MatchResult(m, convertedArgs);
		}
		return result;
	}

	private String getParamNameFromArgAnnotation(Annotation[] annotations) {
		for (Annotation a : annotations) {
			if (a instanceof Var) return ((Var)a).value();
		}
		return null;
	}

	private Object[] convertArgs(Class<?>[] paramTypes, List<String> pathParams) {
		List<Object> result = new ArrayList<Object>();
		int index = 0;
		for (String pathValue : pathParams) {
			result.add(convertValue(paramTypes[index], pathValue));
			index++;
		}
		return result.toArray();
	}

	private Object convertValue(Class<?> cl, String pathValue) {
		if (cl.isPrimitive()) throw new IllegalArgumentException("Primitive types are not supported");
		if (cl.equals(String.class)) return pathValue;

		// for boolean {y,n}, {Y,N} or {true,false} or {TRUE, FALSE}
		if (cl.equals(Boolean.class)) {
			if (pathValue.toLowerCase().equals("y") 
				|| pathValue.toLowerCase().equals("true")) return true;
			
			if (pathValue.toLowerCase().equals("n") 
				|| pathValue.toLowerCase().equals("false")) return false;
			
			return false;
		}

		if (cl.equals(Character.class)) return pathValue.charAt(0);
		if (cl.equals(Byte.class)) return Byte.valueOf(pathValue);
		if (cl.equals(Short.class)) return Short.valueOf(pathValue);
		if (cl.equals(Integer.class)) return Integer.valueOf(pathValue);
		if (cl.equals(Long.class)) return Long.valueOf(pathValue);
		if (cl.equals(Float.class)) return Float.valueOf(pathValue);
		if (cl.equals(Double.class)) return Double.valueOf(pathValue);
		throw new IllegalArgumentException("Unsupported paramter type " + cl.getName());
	}

	/**
	 * Match method to list of parameters by types.
	 * @param restService
	 * @param methodName
	 * @param params
	 * @return
	 */
	private MatchResult matchPathMethod(Object restService, String methodName, 
			List<String> pathParams) {
		MatchResult result = null;
		for (Method m : restService.getClass().getMethods()) {
			if (!m.getName().equals(methodName)) continue;
			if (m.isVarArgs()) 
				throw new IllegalArgumentException("Varargs are not supported");
				
			Class<?>[] paramTypes = m.getParameterTypes();
			if (paramTypes.length != pathParams.size()) continue;
			Object[] convertedArgs = null;
			try {
				convertedArgs = convertArgs(paramTypes, pathParams);
			}
			catch (Exception e) {
			}
			if (convertedArgs == null) continue;
			return new MatchResult(m, convertedArgs);
		}
		return result;
	}
	
}
