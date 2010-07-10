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

import java.util.Collections;
import java.util.List;

import org.mapsto.Filter;
import org.mapsto.Query;
import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.BaseMapstoDaoImpl;
import org.vosao.dao.CommentDao;
import org.vosao.entity.CommentEntity;
import org.vosao.entity.helper.CommentHelper;


/**
 * @author Alexander Oleynik
 */
public class CommentDaoImpl extends BaseMapstoDaoImpl<CommentEntity> 
		implements CommentDao {

	public CommentDaoImpl() {
		super("CommentEntity");
	}

	@Override
	public List<CommentEntity> getByPage(final String pageUrl) {
		Query<CommentEntity> q = newQuery();
		q.addFilter("pageUrl", Filter.EQUAL, pageUrl);
		List<CommentEntity> result = q.select("getByPage", params(pageUrl));
		Collections.sort(result, new CommentHelper.PublishDateDesc());
		return result;
	}
	
	@Override
	public void disable(List<Long> ids) {
		getQueryCache().removeQueries(CommentEntity.class);
		for (Long id : ids) {
			CommentEntity comment = getById(id);
			if (comment != null) {
				comment.setDisabled(true);
				save(comment);
				getEntityCache().removeEntity(CommentEntity.class, id);
			}
		}
	}

	@Override
	public void enable(List<Long> ids) {
		getQueryCache().removeQueries(CommentEntity.class);
		for (Long id : ids) {
			CommentEntity comment = getById(id);
			if (comment != null) {
				comment.setDisabled(false);
				save(comment);
				getEntityCache().removeEntity(CommentEntity.class, id);
			}
		}
	}

	@Override
	public List<CommentEntity> getByPage(String pageUrl, boolean disabled) {
		Query<CommentEntity> q = newQuery();
		q.addFilter("pageUrl", Filter.EQUAL, pageUrl);
		q.addFilter("disabled", Filter.EQUAL, disabled);
		List<CommentEntity> result = q.select("getByPage", params(pageUrl, 
				disabled));
		Collections.sort(result, new CommentHelper.PublishDateDesc());
		return result;
	}

	@Override
	public void removeByPage(String url) {
		Query<CommentEntity> q = newQuery();
		q.addFilter("pageUrl", Filter.EQUAL, url);
		removeSelected(q);
	}
	
}
