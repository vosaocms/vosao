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

package org.vosao.service.back;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.vosao.entity.ConfigEntity;
import org.vosao.service.AbstractService;
import org.vosao.service.ServiceResponse;
import org.vosao.service.vo.SiteStatVO;

/**
 * @author Alexander Oleynik
 */
public interface ConfigService extends AbstractService {
	
	ConfigEntity getConfig();
	
	ServiceResponse saveConfig(final Map<String, String> vo);
	
	ServiceResponse restoreCommentsTemplate() throws IOException;
	
	ServiceResponse reset();
	
	/**
	 * Recreate search index.
	 * @return service response.
	 */
	ServiceResponse reindex();
	
	ServiceResponse cacheReset();
	
	/**
	 * Start export task chain.
	 * @return service response.
	 */
	ServiceResponse startExportTask(String exportType);
	
	boolean isExportTaskFinished(String exportType);
	
	/**
	 * Start export themes task chain.
	 * @return service response.
	 */
	ServiceResponse startExportThemeTask(List<String> ids, 
			List<String> structureIds);

	/**
	 * Start export folder task chain.
	 * @return service response.
	 */
	ServiceResponse startExportFolderTask(Long folderId);
	
	ServiceResponse loadDefaultSite();
	
	SiteStatVO getSiteStat();
	
	ServiceResponse saveAttribute(String name, String value);
	
	ServiceResponse removeAttributes(List<String> names);
	
}
