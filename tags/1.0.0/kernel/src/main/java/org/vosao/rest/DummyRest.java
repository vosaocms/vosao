/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.rest;

//@Authorized
public class DummyRest {

	// /rest/dummy
	public String apply() {
		return "Hello World!";
	}
	
	// /rest/dummy?id=1
	public String apply(@Var("id") Long id) {
		return "Hello World !" + id;
	}

	// /rest/dummy/open/1 
	// /rest/dummy/open?id=1
	public String open(@Var("id") Long id) {
		return "Hello " + id;
	}
	
	// /rest/dummy/test
	public String test() {
		throw new IllegalAccessError();
	}
	
	// /rest/dummy/secure
	@Authorized
	public String secure() {
		return "secret";
	}
}
