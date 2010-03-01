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

package org.vosao.service.back;

import java.util.List;
import java.util.Map;

import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.PageEntity;
import org.vosao.service.AbstractService;
import org.vosao.service.ServiceResponse;
import org.vosao.service.vo.PageRequestVO;
import org.vosao.service.vo.PageVO;

public interface PageService extends AbstractService {
	
	CommentService getCommentService();
	void setCommentService(CommentService bean);

	TemplateService getTemplateService();
	void setTemplateService(TemplateService bean);
	
	LanguageService getLanguageService();
	void setLanguageService(LanguageService bean);
	
	ContentPermissionService getContentPermissionService();
	void setContentPermissionService(ContentPermissionService bean);

	GroupService getGroupService();
	void setGroupService(GroupService bean);

	List<ContentEntity> getContents(final Long pageId);
	
	TreeItemDecorator<PageVO> getTree();
	
	PageEntity getPage(final Long id);
	
	PageRequestVO getPageRequest(final Long id, final String parentUrl);

	PageEntity getPageByUrl(final String url);

	ServiceResponse savePage(final Map<String, String> page);
	
	List<PageVO> getChildren(final String url);
	
	ServiceResponse deletePages(final List<String> ids);

	ServiceResponse deletePageVersion(Long id);

	List<PageVO> getPageVersions(final String url);
	
	/**
	 * Add page next version by copying last version content.
	 * @param url - friendly url.
	 * @return - service response
	 */
	ServiceResponse addVersion(final String url, final String versionTitle);
	
	ServiceResponse approve(final Long pageId);
	
	/**
	 * Restore content of predefined page.
	 * @param pageId - page to restore content
	 * @param pageType - page content to restore (home,login,search)
	 * @param language - content language
	 * @return - service response
	 */
	ServiceResponse restore(final Long pageId, final String pageType,
			final String language);
	
	/**
	 * Move page up in sort order.
	 * @param pageId
	 * @return
	 */
	ServiceResponse moveUp(final Long pageId);

	/**
	 * Move page down in sort order.
	 * @param pageId
	 * @return
	 */
	ServiceResponse moveDown(final Long pageId);
}
