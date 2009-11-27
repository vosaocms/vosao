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

package org.vosao.service.vo;

import java.util.Collections;
import java.util.List;

import org.vosao.entity.ContentEntity;
import org.vosao.entity.ContentPermissionEntity;
import org.vosao.entity.LanguageEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.TemplateEntity;

/**
 * Value object to be returned from services.
 * @author Alexander Oleynik
 */
public class PageRequestVO {

    private PageEntity page;
    private List<PageVO> versions;
    private List<TemplateEntity> templates;
    private List<LanguageEntity> languages;
    private List<PageVO> children;
    private List<CommentVO> comments;
    private List<ContentEntity> contents;
    private List<ContentPermissionVO> permissions;
    private List<GroupVO> groups;
    private ContentPermissionEntity pagePermission;
	
    public PageRequestVO() {
    	versions = Collections.EMPTY_LIST;
    	templates = Collections.EMPTY_LIST;
    	languages = Collections.EMPTY_LIST;
    	children = Collections.EMPTY_LIST;
    	comments = Collections.EMPTY_LIST;
    	contents = Collections.EMPTY_LIST;
    	permissions = Collections.EMPTY_LIST;
    	groups = Collections.EMPTY_LIST;
    }
    
    public PageEntity getPage() {
		return page;
	}
	
    public void setPage(PageEntity page) {
		this.page = page;
	}
	
    public List<PageVO> getVersions() {
		return versions;
	}
	
    public void setVersions(List<PageVO> versions) {
		this.versions = versions;
	}
	
    public List<TemplateEntity> getTemplates() {
		return templates;
	}
	
    public void setTemplates(List<TemplateEntity> templates) {
		this.templates = templates;
	}
	
    public List<LanguageEntity> getLanguages() {
		return languages;
	}
	
    public void setLanguages(List<LanguageEntity> languages) {
		this.languages = languages;
	}
	
    public List<PageVO> getChildren() {
		return children;
	}
	
    public void setChildren(List<PageVO> children) {
		this.children = children;
	}
	
    public List<CommentVO> getComments() {
		return comments;
	}
	
    public void setComments(List<CommentVO> comments) {
		this.comments = comments;
	}
	
    public List<ContentEntity> getContents() {
		return contents;
	}
	
    public void setContents(List<ContentEntity> contents) {
		this.contents = contents;
	}
	
    public List<ContentPermissionVO> getPermissions() {
		return permissions;
	}
	
    public void setPermissions(List<ContentPermissionVO> permissions) {
		this.permissions = permissions;
	}
	
    public List<GroupVO> getGroups() {
		return groups;
	}
	
    public void setGroups(List<GroupVO> groups) {
		this.groups = groups;
	}
	
    public ContentPermissionEntity getPagePermission() {
		return pagePermission;
	}
	
    public void setPagePermission(ContentPermissionEntity pagePermission) {
		this.pagePermission = pagePermission;
	}   
    
}
