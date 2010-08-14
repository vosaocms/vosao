package org.vosao.plugins.backup;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.mq.MessageQueue;
import org.vosao.business.mq.message.ExportMessage;
import org.vosao.business.plugin.PluginCronJob;
import org.vosao.common.VosaoContext;

/**
 * Possible cron parameters:
 * 
 *  every day
 *  every monday
 *  every NN day of month
 *  
 * @author Alexander Oleynik
 *
 */
public class BackupJob implements PluginCronJob {

	private static final Log logger = LogFactory.getLog(
			BackupJob.class);

	private BackupConfig config;
	
	public BackupJob(BackupConfig config) {
		this.config = config;
	}
	
	private MessageQueue getMessageQueue() {
		return VosaoContext.getInstance().getMessageQueue();
	}
	
	@Override
	public void run() {
		getMessageQueue().publish(new ExportMessage.Builder()
			.setFilename("exportFull.vz")
			.setExportType("full").create());
	}

	@Override
	public boolean isShowTime(Date date) {
		String cron = config.getCron();
		try {
			Calendar cal = Calendar.getInstance();
			String[] items = cron.split(" ");
			if (cron.equals("every day")) {
				// start at 01:00
				return cal.get(Calendar.HOUR_OF_DAY) == 1 
					&& cal.get(Calendar.MINUTE) == 0;
			}
			if (cron.endsWith("day of month")) {
				int day = Integer.valueOf(items[1]);
				// start at 01:00
				return cal.get(Calendar.DAY_OF_MONTH) == day
			    	&& cal.get(Calendar.HOUR_OF_DAY) == 1 
			    	&& cal.get(Calendar.MINUTE) == 0;
			}
			int weekDay = getWeekday(items[1]);
			return cal.get(Calendar.DAY_OF_WEEK) == weekDay
	    		&& cal.get(Calendar.HOUR_OF_DAY) == 1 
	    		&& cal.get(Calendar.MINUTE) == 0;
		}
		catch (Exception e) {
			logger.error("Error during parsing backup cron expression. "
					+ e.getMessage());
			return false;
		}
	}

	private int getWeekday(String day) {
		if (day.equals("monday")) {
			return Calendar.MONDAY;
		}
		if (day.equals("tuesday")) {
			return Calendar.TUESDAY;
		}
		if (day.equals("thursday")) {
			return Calendar.THURSDAY;
		}
		if (day.equals("wednesday")) {
			return Calendar.WEDNESDAY;
		}
		if (day.equals("friday")) {
			return Calendar.FRIDAY;
		}
		if (day.equals("saturday")) {
			return Calendar.SATURDAY;
		}
		if (day.equals("sunday")) {
			return Calendar.SUNDAY;
		}
		return Calendar.MONDAY;
	}

}
