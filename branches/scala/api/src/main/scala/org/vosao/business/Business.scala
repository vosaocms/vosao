package org.vosao.business

import org.vosao.dao.Dao
import org.vosao.global.SystemService

trait Business {
	
	def getDao(): Dao

	def getSystemService(): SystemService
	
}