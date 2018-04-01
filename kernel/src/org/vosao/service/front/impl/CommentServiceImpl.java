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

package org.vosao.service.front.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptchaResponse;

import org.vosao.entity.ConfigEntity;
import org.vosao.entity.PageEntity;
import org.vosao.i18n.Messages;
import org.vosao.service.ServiceException;
import org.vosao.service.ServiceResponse;
import org.vosao.service.front.CommentService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.service.vo.CommentVO;
import org.vosao.utils.ParamUtil;
import org.vosao.utils.RecaptchaUtil;

/**
 * @author Alexander Oleynik
 */
public class CommentServiceImpl extends AbstractServiceImpl 
		implements CommentService {

	@Override
	public List<CommentVO> getByPage(String pageUrl) {
		return CommentVO.create(getDao().getCommentDao()
				.getByPage(pageUrl, false));
	}

	@Override
	public ServiceResponse addComment(String name, String comment,
			String pageUrl, String challenge, String response,
			HttpServletRequest request) {
		
		ConfigEntity config = getBusiness().getConfigBusiness().getConfig();
		boolean valid = true;
		ReCaptchaResponse recaptchaResponse = null;
		if (config.isEnableRecaptcha()) {
			recaptchaResponse = RecaptchaUtil.check(
					config.getRecaptchaPublicKey(), 
					config.getRecaptchaPrivateKey(), 
					challenge, response, request);
			valid = recaptchaResponse.isValid();
		}
		if (valid) {
        	try {
        		addComment(ParamUtil.filterXSS(name), 
        				ParamUtil.filterXSS(comment), pageUrl);
                return ServiceResponse.createSuccessResponse(
                		Messages.get("comment_success_create"));
        	}
        	catch (ServiceException e) {
                return ServiceResponse.createErrorResponse(e.getMessage());
        	}
        }
        else {
            return ServiceResponse.createErrorResponse(
            		recaptchaResponse.getErrorMessage());
        }
	}
	
	private void addComment(String name, String content, String pageUrl) 
			throws ServiceException {
		PageEntity page = getDao().getPageDao().getByUrl(pageUrl);
		if (page == null) {
			throw new ServiceException(Messages.get("page_not_found", pageUrl));
		}
		getBusiness().getCommentBusiness().addComment(name, content, page);
	}

}
