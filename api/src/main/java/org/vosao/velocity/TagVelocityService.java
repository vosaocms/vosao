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

package org.vosao.velocity;

import java.util.List;

import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.PageEntity;
import org.vosao.entity.TagEntity;

/**
 * @author Alexander Oleynik
 */
public interface TagVelocityService {

	List<TreeItemDecorator<TagEntity>> getTrees();
	
	TreeItemDecorator<TagEntity> getTree(String name);
	
	List<TagEntity> getTags(String pageURL);
	
	/**
	 * Get all pages by tag id.
	 * @param tagId - tag id
	 * @return found pages.
	 */
	List<PageEntity> getPagesById(Long tagId);
	
	/**
	 * Get all pages by tag id.
	 * @param tagId - tag id
	 * @param index - starting index.
	 * @param count - batch size.
	 * @return found pages.
	 */
	List<PageEntity> getPagesById(Long tagId, int index, int count);

	/**
	 * Get pages of tag selected by tag path in tags tree.
	 * @param tagPath - path to tag like URL. Path starts with /.
	 * @return pages by tag.
	 */
	List<PageEntity> getPagesByPath(String tagPath);

	/**
	 * Get pages of tag selected by tag path in tags tree.
	 * @param tagPath - path to tag like URL. Path starts with /.
	 * @param index - starting index.
	 * @param count - batch size.
	 * @return pages by tag.
	 */
	List<PageEntity> getPagesByPath(String tagPath, int index, int count);

}
