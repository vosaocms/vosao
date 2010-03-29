package org.vosao.update;

public class UpdateTask04 implements UpdateTask {

	@Override
	public String getFromVersion() {
		return "0.3";
	}

	@Override
	public String getToVersion() {
		return "0.4";
	}

	@Override
	public void update() throws UpdateException {
	}

}
