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

package org.vosao.velocity.impl;

import java.util.ArrayList;
import java.util.List;

import org.vosao.dao.Dao;
import org.vosao.entity.PageEntity;
import org.vosao.service.vo.CommentVO;
import org.vosao.velocity.VelocityService;

/**
 * @author Alexander Oleynik
 */
public class VelocityServiceImpl implements VelocityService {

	private Dao dao;
	private String languageCode;
	
	public VelocityServiceImpl(Dao aDao, String aLanguageCode) {
		dao = aDao;
		languageCode = aLanguageCode;
	}
	
	private Dao getDao() {
		return dao;
	}
	
	@Override
	public PageEntity findPage(String path) {
		PageEntity page = getDao().getPageDao().getByUrl(path);
		if (page == null) {
			return new PageEntity("Page not found", "Page not found", 
					"Page not found", null);
		}
		return page;
	}

	@Override
	public List<PageEntity> findPageChildren(String path) {
		return getDao().getPageDao().getByParentApproved(path);
	}

	@Override
	public List<CommentVO> getCommentsByPage(String pageUrl) {
		return CommentVO.create(getDao().getCommentDao().getByPage(
				pageUrl, false));
	}

	@Override
	public String findContent(String path, String aLanguageCode) {
		PageEntity page = getDao().getPageDao().getByUrl(path);
		if (page != null) {
			return getDao().getPageDao().getContent(page.getId(), aLanguageCode);
		}
		return "Approved content not found";
	}

	@Override
	public String findContent(String path) {
		return findContent(path, languageCode);
	}

	@Override
	public List<String> findChildrenContent(String path, String aLanguageCode) {
		List<PageEntity> pages = getDao().getPageDao().getByParentApproved(
				path);
		List<String> result = new ArrayList<String>();
		for (PageEntity page : pages) {
			result.add(getDao().getPageDao().getContent(page.getId(), 
					aLanguageCode));
		}
		return result;
	}

	@Override
	public List<String> findChildrenContent(String path) {
		return findChildrenContent(path, languageCode);
	}
	
}
