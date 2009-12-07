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

package org.vosao.dao.impl;

import java.util.List;

import org.vosao.dao.ContentDao;
import org.vosao.entity.ContentEntity;

public class ContentDaoImpl extends BaseDaoImpl<String, ContentEntity> 
		implements ContentDao {

	public ContentDaoImpl() {
		super(ContentEntity.class);
	}

	@Override
	public List<ContentEntity> select(final String parentClass, 
			final String parentKey) {
		String query = "select from " + ContentEntity.class.getName() 
					+ " where parentClass == pParentClass" 
					+ "   && parentKey == pParentKey" 
					+ " parameters String pParentClass, String pParentKey";
		return select(query, params(parentClass, parentKey));
	}
	
	@Override
	public ContentEntity getByLanguage(final String parentClass, 
			final String parentKey, final String language) {
		String query = "select from " + ContentEntity.class.getName() 
					+ " where parentClass == pParentClass" 
					+ "   && parentKey == pParentKey"
					+ "   && languageCode == pLanguage"
					+ " parameters String pParentClass, String pParentKey," 
					+ "   String pLanguage";
		return selectOne(query, params(parentClass, parentKey, language));
	}
	
}
