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

package org.vosao.global

import javax.xml.transform.Transformer

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine

import com.google.appengine.api.datastore.DatastoreService
import com.google.appengine.api.labs.taskqueue.Queue
import com.google.appengine.api.quota.QuotaService
import com.google.gdata.client.photos.PicasawebService

trait SystemService {
	
	def getCache(): CacheService
	
	def getFileCache(): FileCache
	
	def getDefaultQueue(): Queue

	def getQueue(name: String): Queue

	def getVelocityEngine(): VelocityEngine
	
	def getQuotaService(): QuotaService

	/**
	 * Render velocity template in specified context. 
	 * @param template - template to render.
	 * @param content - context to use.
	 * @return rendered html.
	 */
	def render(template: String, context: VelocityContext): String
	
	def getTransformer(template: String): Transformer
	
	def getDatastore(): DatastoreService
	
	/**
	 * Time duration of current request in seconds. Used in tasks to control
	 * 30 sec limit.
	 * @return request seconds.
	 */
	def getRequestCPUTimeSeconds(): Long
	
}
