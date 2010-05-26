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

package org.vosao.search

import org.vosao.entity.ContentEntity
import org.vosao.entity.PageEntity

trait SearchEngine {

	def updateIndex(page: PageEntity): Unit

	def updateIndex(content: ContentEntity): Unit

	def removeFromIndex(page: PageEntity): Unit

	def removeFromIndex(content: ContentEntity): Unit

	def search(query: String, start: Int, count: Int,
			language: String, textSize: Int): SearchResult
	
	def saveIndex(): Unit
	
	/**
	 * Start index creation procedure. Create index generator task.
	 */
	def reindex(): Unit

	/**
	 * Start index creation procedure. Without task.
	 */
	def reindexInRequest(): Unit
}
