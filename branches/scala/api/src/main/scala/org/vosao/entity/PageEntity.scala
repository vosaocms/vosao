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

package org.vosao.entity

import scala.reflect.BeanProperty
import scala.collection.mutable.ListBuffer
import org.vosao.utils.EntityUtil._
import java.io.UnsupportedEncodingException
import java.util.Date
import org.apache.commons.lang.StringUtils
import org.vosao.enums.PageState
import org.vosao.enums.PageType
import org.vosao.utils.DateUtil
import org.vosao.utils.FolderUtil
import org.vosao.utils.UrlUtil
import com.google.appengine.api.datastore.Entity;

/**
 * @author Alexander Oleynik
 */
class PageEntity extends BaseEntity {

	/**
	 * Titles are stored in string list. Content language stored in first two 
	 * chars. 
	 */
	var title: String
	
	var friendlyURL: String
	
	@BeanProperty
	var parentUrl: String
	
	@BeanProperty
	var template: Long
	
	@BeanProperty
	var publishDate: Date = new Date()
	
	@BeanProperty
	var commentsEnabled: Boolean
	
	@BeanProperty
	var version: Int = 1
	
	@BeanProperty
	var versionTitle: String = "New page"
	
	@BeanProperty
	var state: String = PageState.EDIT
	
	@BeanProperty
	var pageType: String = PageType.SIMPLE
	
	@BeanProperty
	var structureId: Long
	
	@BeanProperty
	var structureTemplateId: Long
	
	@BeanProperty
	var keywords: String = ""
	
	@BeanProperty
	var description: String = ""
	
	@BeanProperty
	var searchable: Boolean = true
	
	@BeanProperty
	var sortIndex: Int = 0
	
	@BeanProperty
	var velocityProcessing: Boolean = false
	
	@BeanProperty
	var headHtml: String = ""
	
	@BeanProperty
	var skipPostProcessing: Boolean = false

	// not persisted
	@BeanProperty
	var titles: Map[String, String]
	
	override def load(entity: Entity) {
		super.load(entity)
		title = getTextProperty(entity, "title")
		friendlyURL = getStringProperty(entity, "friendlyURL")
		parentUrl = getStringProperty(entity, "parentUrl")
		template = getLongProperty(entity, "template")
		publishDate = getDateProperty(entity, "publishDate")
		commentsEnabled = getBooleanProperty(entity, "commentsEnabled", false)
		version = getIntegerProperty(entity, "version", 1)
		versionTitle = getStringProperty(entity, "versionTitle")
		state = getStringProperty(entity, "state")
		pageType = getStringProperty(entity, "pageType")
		structureId = getLongProperty(entity, "structureId")
		structureTemplateId = getLongProperty(entity, "structureTemplateId")
		keywords = getTextProperty(entity, "keywords")
		description = getTextProperty(entity, "description")
		searchable = getBooleanProperty(entity, "searchable", true)
		sortIndex = getIntegerProperty(entity, "sortIndex", 0)
		velocityProcessing = getBooleanProperty(entity, "velocityProcessing", false)
		headHtml = getTextProperty(entity, "headHtml")
		skipPostProcessing = getBooleanProperty(entity, "skipPostProcessing", false)
	}
	
	override def save(entity: Entity) {
		super.save(entity)
		setTextProperty(entity, "title", title)
		setProperty(entity, "friendlyURL", friendlyURL, true)
		setProperty(entity, "parentUrl", parentUrl, true)
		setProperty(entity, "template", template, true)
		setProperty(entity, "publishDate", publishDate, false)
		setProperty(entity, "commentsEnabled", commentsEnabled, false)
		setProperty(entity, "version", version, true)
		setProperty(entity, "versionTitle", versionTitle, false)
		setProperty(entity, "state", state, false)
		setProperty(entity, "pageType", pageType, false)
		setProperty(entity, "structureId", structureId, true)
		setProperty(entity, "structureTemplateId", structureTemplateId, true)
		setTextProperty(entity, "keywords", keywords)
		setTextProperty(entity, "description", description)
		setProperty(entity, "searchable", searchable, false)
		setProperty(entity, "sortIndex", sortIndex, false)
		setProperty(entity, "velocityProcessing", velocityProcessing, false)
		setTextProperty(entity, "headHtml", headHtml)
		setProperty(entity, "skipPostProcessing", skipPostProcessing, false)
	}

	def this(aTitle: String, aFriendlyURL: String) {
		this()
		title = aTitle
		setFriendlyURL(aFriendlyURL)
	}

	def this(title: String, friendlyURL: String, aTemplate: Long) {
		this(title, friendlyURL)
		template = aTemplate
	}

	def this(aTitle: String, aFriendlyURL: String, aTemplate: Long, 
			publish: Date) {
		this(aTitle, aFriendlyURL, aTemplate)
		publishDate = publish
	}

	def getFriendlyURL() = friendlyURL
	
	def setFriendlyURL(aFriendlyURL: String) {
		friendlyURL = aFriendlyURL
		parentUrl = getParentFriendlyURL()
	}

	def getParentFriendlyURL() = UrlUtil.getParentFriendlyURL(getFriendlyURL)

	def setParentFriendlyURL(url: String) {
		if (getFriendlyURL == null) {
			setFriendlyURL(url)
		}
		else {
			setFriendlyURL(url + "/" + getPageFriendlyURL)
		}
	}

	def getPageFriendlyURL() = UrlUtil.getNameFromFriendlyURL(getFriendlyURL)

	def setPageFriendlyURL(url: String) {
		if (getFriendlyURL == null) {
			setFriendlyURL(url)
		}
		else {
			if (parentUrl == "/") {
				friendlyURL = parentUrl + url
			}
			else {
				friendlyURL = parentUrl + "/" + url
			}
		}
	}

	def PublishDateString() = DateUtil.toString(publishDate)
	
	def isRoot() = friendlyURL == "/"

	def isApproved() = state == PageState.APPROVED

	def isSimple() = pageType == PageType.SIMPLE

	def isStructured() = pageType == PageType.STRUCTURED

	def getTitleValue() = title

	def setTitleValue(t: String) {
		title = t
		parseTitle()
	}

	def getTitle() = getLocalTitle("en")

	def setTitle(title: String) {
		setLocalTitle(title, "en")
	}

	def getLocalTitle(lang: String) = {
		parseTitle()
		if ("en" != lang) {
			if (StringUtils.isEmpty(titles(lang))) titles.get("en")
		}
		titles.get(lang)
	}

	def setLocalTitle(title: String, lang: String) {
		parseTitle()
		titles(lang) = title
		packTitle()
	}
	
	private def parseTitle() {
		if (title == null) titles = Map()
		else 
			for (s <- getTitleValue().split(",")) 
				titles(s.substring(0, 2)) = s.substring(2) 
	}
	
	private def packTitle() {
		if (titles != null) {
			val s = new StringBuffer()
			val count = 0
			for ((key, value) <- titles) {
				if (count > 0) s.append(",") 
				s.append(key).append(value)
			}
			setTitleValue(s.toString)
		}
	}

	def getAncestorsURL(): List[String] = {
		var result = new ListBuffer[String]()
		if (isRoot) {
			result += getFriendlyURL
			result.toList
		}
		if (getParentUrl == "/") {
			result += getFriendlyURL
			result.toList
		}
		val s = new StringBuffer()
		for (p <- FolderUtil.getPathChain(getFriendlyURL)) {
			 s.append("/").append(p)
			 result += s.toString
		}
		result.toList
	}
}
