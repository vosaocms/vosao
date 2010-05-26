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
import org.vosao.utils.EntityUtil._
import com.google.appengine.api.datastore.Entity;

class ConfigEntity extends BaseEntity {

	@BeanProperty
	var googleAnalyticsId: String
	
	@BeanProperty
	var siteEmail: String
	
	@BeanProperty
	var siteDomain: String
	
	@BeanProperty
	var editExt: String
	
	@BeanProperty
	var enableRecaptcha: Boolean
	
	@BeanProperty
	var recaptchaPrivateKey: String
	
	@BeanProperty
	var recaptchaPublicKey: String
	
	@BeanProperty
	var commentsEmail: String
	
	@BeanProperty
	var commentsTemplate: String = ""
	
	@BeanProperty
	var version: String
	
	@BeanProperty
	var siteUserLoginUrl: String
	
	@BeanProperty
	var enablePicasa: Boolean
	
	@BeanProperty
	var picasaUser: String
	
	@BeanProperty
	var picasaPassword: String
	
	override def load(entity: Entity) {
		super.load(entity)
		googleAnalyticsId = getStringProperty(entity, "googleAnalyticsId")
		siteEmail = getStringProperty(entity, "siteEmail")
		siteDomain = getStringProperty(entity, "siteDomain")
		editExt = getStringProperty(entity, "editExt")
		enableRecaptcha = getBooleanProperty(entity, "enableRecaptcha", false)
		recaptchaPrivateKey = getStringProperty(entity, "recaptchaPrivateKey")
		recaptchaPublicKey = getStringProperty(entity, "recaptchaPublicKey")
		commentsEmail = getStringProperty(entity, "commentsEmail")
		commentsTemplate = getTextProperty(entity, "commentsTemplate")
		version = getStringProperty(entity, "version")
		siteUserLoginUrl = getStringProperty(entity, "siteUserLoginUrl")
		enablePicasa = getBooleanProperty(entity, "enablePicasa", false)
		picasaUser = getStringProperty(entity, "picasaUser")
		picasaPassword = getStringProperty(entity, "picasaPassword")
	}
	
	override def save(entity: Entity) {
		super.save(entity)
		setProperty(entity, "googleAnalyticsId", googleAnalyticsId, false)
		setProperty(entity, "siteEmail", siteEmail, false)
		setProperty(entity, "siteDomain", siteDomain, false)
		setProperty(entity, "editExt", editExt, false)
		setProperty(entity, "enableRecaptcha", enableRecaptcha, false)
		setProperty(entity, "recaptchaPrivateKey", recaptchaPrivateKey, false)
		setProperty(entity, "recaptchaPublicKey", recaptchaPublicKey, false)
		setProperty(entity, "commentsEmail", commentsEmail, false)
		setTextProperty(entity, "commentsTemplate", commentsTemplate)
		setProperty(entity, "version", version, false)
		setProperty(entity, "siteUserLoginUrl", siteUserLoginUrl, false)
		setProperty(entity, "enablePicasa", enablePicasa, false)
		setProperty(entity, "picasaUser", picasaUser, false)
		setProperty(entity, "picasaPassword", picasaPassword, false)
	}

}
