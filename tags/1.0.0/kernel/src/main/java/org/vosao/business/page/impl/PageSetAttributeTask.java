package org.vosao.business.page.impl;

import java.util.List;

import org.vosao.business.impl.mq.AbstractSubscriber;
import org.vosao.business.mq.Message;
import org.vosao.entity.PageEntity;

public class PageSetAttributeTask extends AbstractSubscriber {

	@Override
	public void onMessage(Message message) {
		PageSetAttributeMessage msg = (PageSetAttributeMessage)message;
		List<PageEntity> versions = getDao().getPageDao().selectByUrl(
				msg.getUrl());
		for (PageEntity version : versions) {
			version.setAttribute(msg.getName(), msg.getLanguage(), 
					msg.getValue());
			getDao().getPageDao().save(version);
		}
		if (!versions.isEmpty()) {
			List<PageEntity> children = getDao().getPageDao().getByParent(
					versions.get(0).getFriendlyURL());
			for (PageEntity child : children) {
				getBusiness().getMessageQueue().publish(
						new PageSetAttributeMessage(child.getFriendlyURL(), 
								msg.getName(), msg.getLanguage(), 
								msg.getValue()));
			}
		}
	}

}
