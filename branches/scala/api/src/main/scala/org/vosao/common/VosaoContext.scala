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

package org.vosao.common;

import javax.servlet.http.HttpServletRequest

import org.vosao.entity.UserEntity

/**
 * Because of GAE is single threaded we can store request scoped data in 
 * singleton and set it in filter.
 * 
 * @author Alexander Oleynik
 */
object VosaoContext {

	private var request: HttpServletRequest = null
	private var requestCount = 0
	private var startTime = System.currentTimeMillis()
	
	private var language: String = null
	private var user: UserEntity = null
	
	def getRequest() = request

	def setRequest(request: HttpServletRequest) {
		this.request = request;
		requestCount = requestCount + 1
		startTime = System.currentTimeMillis
	}
	
	def getLanguage() = language

	def setLanguage(language: String) {
		this.language = language
	}

	def getUser() = user
	
	def setUser(user: UserEntity) {
		this.user = user
	}

	def getRequestCount() = requestCount

	def getStartTime() = startTime
	
}
