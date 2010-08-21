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

package org.vosao.update.verion_0_7;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.vosao.business.impl.mq.AbstractSubscriber;
import org.vosao.business.mq.Message;
import org.vosao.business.mq.message.SimpleMessage;
import org.vosao.entity.PageEntity;
import org.vosao.utils.StrUtil;

/**
 * Update titles format from CSV to JSON.
 * 
 * @author Alexander Oleynik
 *
 */
public class PageTitleUpdate extends AbstractSubscriber {

	@Override
	public void onMessage(Message message) {
		logger.info("PageTitleUpdate 0.7 task started...");
		List<PageEntity> pages = getDao().getPageDao().select();
		for (PageEntity page : pages) {
			if (StringUtils.isEmpty(page.getTitle())) {
				Map<String, String> map = StrUtil.unpack06Title(
						page.getTitleValue());
				for (String key : map.keySet()) {
					page.setLocalTitle(map.get(key), key);
				}
				getDao().getPageDao().save(page);
			}
			if (getBusiness().getSystemService()
					.getRequestCPUTimeSeconds() > 20) {
				SimpleMessage msg = new SimpleMessage(PageTitleUpdate.class);
				getBusiness().getMessageQueue().publish(msg);
				return;
			}
		}
		logger.info("PageTitleUpdate 0.7 task completed.");
	}
}
