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
import org.vosao.business.mq.message.IndexMessage;
import org.vosao.common.RequestTimeoutException;
import org.vosao.common.VosaoContext;
import org.vosao.entity.PageEntity;
import org.vosao.entity.helper.UserHelper;

/**
 * Reindex pages.
 * 
 * @author Alexander Oleynik
 *
 */
public class Reindex extends AbstractSubscriber {

	private int currentNumber = 0;
	private int count = 0;
	private IndexMessage msg;
	
	public void onMessage(Message message) {
		try {
			VosaoContext.getInstance().setUser(UserHelper.ADMIN);
			msg = (IndexMessage)message;
			reindexPage(getDao().getPageDao().getByUrl("/"));
			getBusiness().getSearchEngine().saveIndex();
			logger.info("Reindex finished. Reindexed " + count + " pages.");
		}
		catch (RequestTimeoutException e) {
			getBusiness().getSearchEngine().saveIndex();
			getBusiness().getMessageQueue().publish(new IndexMessage(
					currentNumber));
			logger.info("Reindexed " + count + " pages");
		}
		catch(Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void reindexPage(PageEntity page) throws RequestTimeoutException {
		currentNumber++;
		if (getBusiness().getSystemService().getRequestCPUTimeSeconds() > 20) {
			throw new RequestTimeoutException();
		}
		if (msg.getNumber() <= currentNumber) {
			getBusiness().getSearchEngine().updateIndex(page.getId());
			count++;
		}
		for (PageEntity child : getDao().getPageDao().getByParent(
				page.getFriendlyURL())) {
			reindexPage(child);
		}
	}

}