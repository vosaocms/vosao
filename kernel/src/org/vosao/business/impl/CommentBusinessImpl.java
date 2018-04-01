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

package org.vosao.business.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.vosao.business.CommentBusiness;
import org.vosao.business.ContentPermissionBusiness;
import org.vosao.common.VosaoContext;
import org.vosao.entity.CommentEntity;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.ContentPermissionEntity;
import org.vosao.entity.PageEntity;
import org.vosao.utils.EmailUtil;
import org.vosao.utils.StrUtil;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class CommentBusinessImpl extends AbstractBusinessImpl 
	implements CommentBusiness {

	private static final String COMMENT_LETTER_SUBJECT = "New comment";
	
	@Override
	public CommentEntity addComment(String name, String content, 
			PageEntity page) {

		ConfigEntity config = VosaoContext.getInstance().getConfig();
		CommentEntity comment = new CommentEntity(name, content, 
				new Date(), page.getFriendlyURL());
		getDao().getCommentDao().save(comment);
		getBusiness().getSystemService().getPageCache().remove(
				page.getFriendlyURL());
		List<String> toAddresses = StrUtil.fromCSV(config.getCommentsEmail());
		if (toAddresses.size() == 0) {
			toAddresses.add(config.getSiteEmail());
		}
		for (String email : toAddresses) {
			EmailUtil.sendEmail(createCommentLetter(comment, page), 
				COMMENT_LETTER_SUBJECT, 
				config.getSiteEmail(), 
				config.getSiteDomain() + " admin", 
				StringUtils.strip(email));
			logger.debug("New comment letter was sent to " + email);
		}
		return comment;
	}
	
	private String createCommentLetter(CommentEntity comment, PageEntity page) {
		ConfigEntity config = VosaoContext.getInstance().getConfig();
		StringBuffer b = new StringBuffer();
		b.append("<p>New comment was added to page ")
		    .append(config.getSiteDomain()).append(page.getFriendlyURL())
		    .append(" by ").append(comment.getName()).append("</p>")
		    .append(comment.getContent());
		return b.toString();
	}

	private boolean isChangeGranted(List<Long> ids) {
		if (ids.size() > 0) {
			CommentEntity comment = getDao().getCommentDao().getById(ids.get(0));
			ContentPermissionEntity permission = getContentPermissionBusiness()
					.getPermission(	comment.getPageUrl(), 
							VosaoContext.getInstance().getUser());
			if (permission != null) {
				return permission.isChangeGranted();
			}
		}		
		return false;
	}
	
	@Override
	public void disable(List<Long> ids) {
		clearPageCache(ids);
		if (isChangeGranted(ids)) {
			getDao().getCommentDao().disable(ids);
		}		
	}

	@Override
	public void enable(List<Long> ids) {
		clearPageCache(ids);
		if (isChangeGranted(ids)) {
			getDao().getCommentDao().enable(ids);
		}		
	}

	@Override
	public void remove(List<Long> ids) {
		clearPageCache(ids);
		if (isChangeGranted(ids)) {
			getDao().getCommentDao().remove(ids);
		}		
	}

	private void clearPageCache(List<Long> commentIds) {
		for(Long id : commentIds) {
			CommentEntity comment = getDao().getCommentDao().getById(id);
			if (comment != null) {
				getBusiness().getSystemService().getPageCache().remove(
						comment.getPageUrl());
			}
		}
	}

	private ContentPermissionBusiness getContentPermissionBusiness() {
		return getBusiness().getContentPermissionBusiness();
	}
	
}
