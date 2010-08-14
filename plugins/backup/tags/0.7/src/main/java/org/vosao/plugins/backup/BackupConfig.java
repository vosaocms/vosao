package org.vosao.plugins.backup;


public class BackupConfig {

	private String cron;
	
	public BackupConfig() {	
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}
	
}
