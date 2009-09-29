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

package org.vosao.business.impl;

import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.CommentBusiness;
import org.vosao.entity.CommentEntity;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.PageEntity;
import org.vosao.utils.EmailUtil;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

public class CommentBusinessImpl extends AbstractBusinessImpl 
	implements CommentBusiness {

	private static final Log logger = LogFactory.getLog(FileBusinessImpl.class);

	private static final String COMMENT_LETTER_SUBJECT = "New comment";
	
	@Override
	public CommentEntity addComment(String name, String content, 
			PageEntity page) {
		ConfigEntity config = getDao().getConfigDao().getConfig();
		String encodedContent = StringEscapeUtils.escapeHtml(content);
		CommentEntity comment = new CommentEntity(name, encodedContent, 
				new Date(), page.getId());
		getDao().getCommentDao().save(comment);
		String toAddress = config.getCommentsEmail();
		if (StringUtil.isEmpty(toAddress)) {
			toAddress = config.getSiteEmail();
		}
		EmailUtil.sendEmail(createCommentLetter(comment, page), 
				COMMENT_LETTER_SUBJECT, 
				config.getSiteEmail(), 
				config.getSiteDomain() + " admin", 
				toAddress);
		logger.debug("New comment letter was sent to " + toAddress);
		return comment;
	}
	
	private String createCommentLetter(CommentEntity comment, PageEntity page) {
		ConfigEntity config = getDao().getConfigDao().getConfig();
		StringBuffer b = new StringBuffer();
		b.append("<p>New comment was added to page ")
		    .append(config.getSiteDomain()).append(page.getFriendlyURL())
		    .append("by ").append(comment.getName()).append("</p>")
		    .append(comment.getContent());
		return b.toString();
	}

	
}
