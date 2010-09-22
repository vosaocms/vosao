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

package org.vosao.business.impl.mq.subscriber;

import org.vosao.business.impl.mq.AbstractSubscriber;
import org.vosao.business.mq.Message;
import org.vosao.business.mq.Topic;
import org.vosao.business.mq.message.PageMessage;
import org.vosao.entity.PageEntity;
import org.vosao.enums.PageState;

/**
 * Reset cache for fresh scheduled published and unpublished pages.
 * 
 * @author Alexander Oleynik
 *
 */
public class PagePublishCron extends AbstractSubscriber {

	public void onMessage(Message message) {
		logger.info("Page publish cron...");
		for (PageEntity page : getDao().getPageDao()
				.getCurrentHourPublishedPages()) {
			if (page.getState().equals(PageState.APPROVED)) {
				logger.info("Found published " + page.getFriendlyURL());
				getBusiness().getSystemService().getPageCache().remove(
					page.getFriendlyURL());
				getBusiness().getMessageQueue().publish(new PageMessage(
					Topic.PAGE_CACHE_CLEAR, page.getFriendlyURL(), page.getId()));
			}
		}
		for (PageEntity page : getDao().getPageDao()
				.getCurrentHourUnpublishedPages()) {
			if (page.getState().equals(PageState.APPROVED)) {
				logger.info("Found unpublished " + page.getFriendlyURL());
				getBusiness().getSystemService().getPageCache().remove(
					page.getFriendlyURL());
				getBusiness().getMessageQueue().publish(new PageMessage(
					Topic.PAGE_CACHE_CLEAR, page.getFriendlyURL(), page.getId()));
			}
		}
	}
}