package org.vosao.global

import org.vosao.entity.FileEntity

trait FileCache {

	def isInCache(path: String): Boolean
	
	def isInPublicCache(path: String): Boolean
	
	def put(path: String, file: FileEntity, content: Array[Byte]): Unit
	
	def getFile(path: String): FileEntity
	
	def getContent(path: String): Array[Byte]
	
	def makePublic(path: String): Unit

	def remove(path: String): Unit
	
}
