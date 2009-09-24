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

package org.vosao.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jabsorb.JSONRPCBridge;
import org.vosao.service.FileService;
import org.vosao.service.FormService;
import org.vosao.service.Service;
import org.vosao.service.TestService;

public class ServiceImpl implements Service {

	private static final Log log = LogFactory.getLog(ServiceImpl.class);

	private TestService testService;
	private FormService formService;
	private FileService fileService;
	
	public void init() {
		JSONRPCBridge.getGlobalBridge().registerObject("testService", testService);
		JSONRPCBridge.getGlobalBridge().registerObject("formService", formService);
		JSONRPCBridge.getGlobalBridge().registerObject("fileService", fileService);
	}
	
	@Override
	public TestService getTestService() {
		return testService;
	}

	@Override
	public void setTestService(TestService bean) {
		testService = bean;		
	}

	@Override
	public FormService getFormService() {
		return formService;
	}

	@Override
	public void setFormService(FormService bean) {
		formService = bean;
	}

	@Override
	public FileService getFileService() {
		return fileService;
	}

	@Override
	public void setFileService(FileService bean) {
		fileService = bean;
	}

}
