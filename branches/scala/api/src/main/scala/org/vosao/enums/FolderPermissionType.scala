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

package org.vosao.enums

/**
 * @author Alexander Oleynik
 */
object FolderPermissionType {
	val DENIED = "DENIED"
	val READ = "READ"
	val WRITE= "WRITE"
	val ADMIN = "ADMIN"
	
	def fromContentPermissionType(perm: String): String = perm match {
		case ContentPermissionType.DENIED => DENIED
		case ContentPermissionType.READ => READ
		case ContentPermissionType.ADMIN => ADMIN
		case _ => WRITE
	}
	
	def getOrdinal(permission: String) = permission match {
		case DENIED => 0
		case READ => 1
		case WRITE => 2
		case ADMIN => 3
		case _ => 0
	}
		
	def isHigher(p1: String, p2: String) = getOrdinal(p1) > getOrdinal(p2)

}
