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

package org.vosao.business.impl.pagefilter;

import org.apache.commons.lang.StringUtils;
import org.vosao.entity.PageEntity;
import org.vosao.utils.StrUtil;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class MetaPageFilter implements PageFilter {
	
	@Override
	public String apply(String content, PageEntity page) {
		String result = content;
		if (!StringUtils.isEmpty(page.getDescription())) {
			String description = "<meta name=\"description\" content=\"" 
					+ page.getDescription() + "\" />"; 
			if (StrUtil.DESCRIPTION_PATTERN.matcher(result).find()) {
				result = result.replaceAll(StrUtil.DESCRIPTION_REGEX,
						description);
			}
			else {
				result = result.replaceAll(StrUtil.HEAD_CLOSE_REGEX, description
						+ "\n</head>");
			}
		}
		if (!StringUtils.isEmpty(page.getKeywords())) {
			String keywords = "<meta name=\"keywords\" content=\"" 
					+ page.getKeywords() + "\" />";
			if (StrUtil.KEYWORDS_PATTERN.matcher(result).find()) {
				result = result.replaceAll(StrUtil.KEYWORDS_REGEX,
						keywords);
			}
			else {
				result = result.replaceAll(StrUtil.HEAD_CLOSE_REGEX, keywords
						+ "\n</head>");
			}
		}
		return result;
	}

}
