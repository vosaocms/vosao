package org.vosao.entity

import scala.reflect.BeanProperty

import org.vosao.utils.EntityUtil._

import java.util.Date

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.vosao.utils.DateUtil

import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.Key
import com.google.appengine.api.datastore.KeyFactory

@serializable
abstract class BaseEntity {

	@BeanProperty
	var key: Key = null
	
	@BeanProperty
	var createUserEmail: String = ""
	
	@BeanProperty
	var createDate: Date = new Date
	
	@BeanProperty
	var modUserEmail: String = ""
	
	@BeanProperty
	var modDate: Date = createDate

	def getId() = if (key == null) null else key.getId

	def setId(id: Long) {
		if (id != null && id > 0) {
			key = KeyFactory.createKey(getKind(getClass()), id)
		}
	}

	def load(entity: Entity) {
		key = entity.getKey
		createUserEmail = getStringProperty(entity, "createUserEmail")
		createDate = getDateProperty(entity, "createDate")
		modUserEmail = getStringProperty(entity, "modUserEmail")
		modDate = getDateProperty(entity, "modDate")
	}

	def save(entity: Entity) {
		setProperty(entity, "createUserEmail", createUserEmail, false)
		setProperty(entity, "createDate", createDate, false)
		setProperty(entity, "modUserEmail", modUserEmail, false)
		setProperty(entity, "modDate", modDate, false)
	}

	def isNew() = key == null

	def copy(entity: BaseEntity) {
		val myKey = getKey 
		val buf = new Entity("tmp")
		entity.save(buf)
		load(buf)
		setKey(myKey)
	}
	
	override def equals(obj: AnyRef): Boolean = {
		if (obj.isInstanceOf[BaseEntity]
			&& obj.getClass.equals(this.getClass)) {
			val entity = obj.asInstanceOf[BaseEntity]
			if (getId() == null && entity.getId() == null) true
			if (getId() != null && getId().equals(entity.getId())) true
		}
		false;
	}

}
