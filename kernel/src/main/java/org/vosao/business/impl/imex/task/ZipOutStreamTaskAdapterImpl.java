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

package org.vosao.business.impl.imex.task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.imex.task.TaskTimeoutException;
import org.vosao.business.imex.task.ZipOutStreamTaskAdapter;

public class ZipOutStreamTaskAdapterImpl implements ZipOutStreamTaskAdapter {

	protected static final Log logger = LogFactory
			.getLog(ZipOutStreamTaskAdapterImpl.class);

	private ByteArrayOutputStream outData;
	private ZipOutputStream outStream;
	private String startFile;
	private String currentFile;
	private long startTime;
	private boolean started;
	private boolean entryOpen;
	
	public ZipOutStreamTaskAdapterImpl(ZipOutputStream out, 
			ByteArrayOutputStream anOutData) {
		this();
		out = outStream;
		outData = anOutData;
	}

	public ZipOutStreamTaskAdapterImpl() {
		started = false;
		entryOpen = false;
		startTime = System.currentTimeMillis();
	}

	@Override
	public ZipOutputStream getOutStream() {
		return outStream;
	}

	@Override
	public void setOutStream(ZipOutputStream out) {
		this.outStream = out;
	}

	@Override
	public String getStartFile() {
		return startFile;
	}

	@Override
	public void setStartFile(String startFile) {
		this.startFile = startFile;
		if (startFile == null) {
			started = true;
		}
	}

	@Override
	public String getCurrentFile() {
		return currentFile;
	}

	@Override
	public void setCurrentFile(String currentFile) {
		this.currentFile = currentFile;
	}
	
	@Override
	public boolean isSkip(String filePath) {
		if (started) {
			return false;
		}
		if (startFile == null) {
			started = true;
			return false;
		}
		if (filePath.equals(startFile)) {
			started = true;
			return false;
		}
		return true;
	}

	private void checkTimeout() throws TaskTimeoutException {
		if (System.currentTimeMillis() - startTime > 24000) {
			throw new TaskTimeoutException();
		}
	}
	
	@Override
	public void putNextEntry(ZipEntry entry) throws IOException, 
			TaskTimeoutException {
		currentFile = entry.getName();
		if (!isSkip(entry.getName())) {
			checkTimeout();
			getOutStream().putNextEntry(entry);
			entryOpen = true;
		}
	}
	
	@Override
	public void closeEntry() throws IOException {
		if (started && entryOpen) {
			getOutStream().closeEntry();
			entryOpen = false;
		}
	}
	
	@Override
	public void write(byte[] data) throws IOException {
		if (started) {
			getOutStream().write(data);
		}
	}

	@Override
	public ByteArrayOutputStream getOutData() {
		return outData;
	}

	@Override
	public void setOutData(ByteArrayOutputStream outData) {
		this.outData = outData;		
	}
}
