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

package org.vosao.velocity.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.vosao.business.Business;
import org.vosao.business.page.impl.StructurePageRenderDecorator;
import org.vosao.business.vo.StructureFieldVO;
import org.vosao.common.AbstractServiceBeanImpl;
import org.vosao.common.VosaoContext;
import org.vosao.entity.CommentEntity;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.PageTagEntity;
import org.vosao.entity.StructureEntity;
import org.vosao.entity.StructureTemplateEntity;
import org.vosao.entity.TagEntity;
import org.vosao.entity.UserEntity;
import org.vosao.entity.helper.PageHelper;
import org.vosao.enums.UserRole;
import org.vosao.i18n.Messages;
import org.vosao.search.SearchResultFilter;
import org.vosao.service.front.impl.SectionSearchFilter;
import org.vosao.service.vo.CommentVO;
import org.vosao.service.vo.FileVO;
import org.vosao.service.vo.UserVO;
import org.vosao.utils.DateUtil;
import org.vosao.utils.ListUtil;
import org.vosao.velocity.PicasaVelocityService;
import org.vosao.velocity.TagVelocityService;
import org.vosao.velocity.VelocityService;

/**
 * @author Alexander Oleynik
 */
public class VelocityServiceImpl extends AbstractServiceBeanImpl 
		implements VelocityService {

	private TagVelocityService tagVelocityService;
	private PicasaVelocityService picasaVelocityService;
	
	public VelocityServiceImpl(Business business) {
		super(business);
	}
	
	@Override
	public PageEntity findPage(String path) {
		PageEntity page = getBusiness().getPageBusiness().getByUrl(path);
		if (page == null) {
			return new PageEntity(Messages.get("page.not_found", path), 
					Messages.get("page.not_found", path), null);
		}
		return page;
	}

	@Override
	public List<PageEntity> findPageChildren(String path) {
		return getBusiness().getPageBusiness().getByParentApproved(path);
	}

	@Override
	public List<CommentVO> getCommentsByPage(String pageUrl) {
		// TODO add security check
		return CommentVO.create(getDao().getCommentDao().getByPage(
				pageUrl, false));
	}

	@Override
	public List<CommentVO> getCommentsByPage(String pageUrl, String ascdesc) {
		// TODO add security check
		return CommentVO.create(getDao().getCommentDao().getByPage(
				pageUrl, false, ascdesc));
	}
	
	
	@Override
	public String findContent(String path, String aLanguageCode) {
		PageEntity page = getBusiness().getPageBusiness().getByUrl(path);
		if (page != null) {
			return getBusiness().getPageBusiness().createPageRenderDecorator(
				page, aLanguageCode).getContent();
		}
		return Messages.get("approved_content_not_found");
	}

	@Override
	public String findStructureContent(String path, String field) {
		return findStructureContent(path, field, getBusiness().getLanguage());
	}
	
	@Override
	public String findStructureContent(String path, String field,
			String aLanguageCode) {
		PageEntity page = getBusiness().getPageBusiness().getByUrl(path);
		if (page != null) {
			if (field != null) {
				if (page.isStructured() 
					&& isStructureFieldExists(page, field)) {
					StructurePageRenderDecorator pageDecorator = 
						(StructurePageRenderDecorator) getBusiness()
							.getPageBusiness().createPageRenderDecorator(
									page, aLanguageCode);
					String content = pageDecorator.getContentMap().get(field);
					return content != null ? content : "";
				}
			}
			return "";
		}
		return Messages.get("approved_content_not_found");
	}

	
	private boolean isStructureFieldExists(PageEntity page, String fieldName) {
		StructureEntity structure = getDao().getStructureDao().getById(
				page.getStructureId());
		List<StructureFieldVO> fields = structure.getFields();
		for (StructureFieldVO field : fields) {
			if (field.getName().equals(fieldName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String findContent(String path) {
		return findContent(path, getBusiness().getLanguage());
	}

	@Override
	public List<String> findChildrenContent(String path, String aLanguageCode) {
		List<PageEntity> pages = getBusiness().getPageBusiness().getByParentApproved(path);
		List<String> result = new ArrayList<String>();
		for (PageEntity page : pages) {
			result.add(getBusiness().getPageBusiness()
					.createPageRenderDecorator(page, aLanguageCode)
					.getContent());
		}
		return result;
	}

	@Override
	public List<String> findChildrenContent(String path) {
		return findChildrenContent(path, getBusiness().getLanguage());
	}

	@Override
	public UserVO findUser(String email) {
		UserEntity user = getDao().getUserDao().getByEmail(email);
		if (user == null) {
			user = new UserEntity(Messages.get("not_found"),
					Messages.get("not_found"), Messages.get("not_found"),
					UserRole.USER);
		}
		return new UserVO(user);
	}

	@Override
	public List<PageEntity> findPageChildren(final String path, 
			final Date publishDate) {
		return ListUtil.filter(findPageChildren(path),
				new ListUtil.Filter<PageEntity>() {
					@Override
					public boolean filter(PageEntity entity) {
						return entity.getPublishDate().equals(publishDate);
					}
				});
	}

	@Override
	public List<PageEntity> findPageChildren(String path, int count) {
		List<PageEntity> list = findPageChildren(path);
		if (list.size() > count) {
			return findPageChildren(path).subList(0, count);
		}
		return list;
	}

	@Override
	public List<PageEntity> findPageChildrenOrdered(String path) {
		List<PageEntity> result = findPageChildren(path);
		Collections.sort(result, PageHelper.SORT_INDEX_ASC);
		return result;
	}

	@Override
	public List<PageEntity> findPageChildrenOrdered(String path, int count) {
		List<PageEntity> result = findPageChildren(path, count);
		Collections.sort(result, PageHelper.SORT_INDEX_ASC);
		return result;
	}

	@Override
	public TagVelocityService getTag() {
		if (tagVelocityService == null) {
			tagVelocityService = new TagVelocityServiceImpl(getBusiness());
		}
		return tagVelocityService;
	}

	@Override
	public List<PageEntity> findPageChildren(String path, int start, int count) {
		return ListUtil.slice(findPageChildren(path), start, count);
	}

	@Override
	public String renderStructureContent(String path,
			String structureTemplateName) {
		
		PageEntity page = getBusiness().getPageBusiness().getByUrl(path);
		StructureTemplateEntity template = null;
		
		if (page != null) {
			if (!page.isStructured()) {
				return Messages.get("page.not_structural");
			}
			List <StructureTemplateEntity> result = getDao()
					.getStructureTemplateDao().selectByStructure(page.getStructureId());
			
			for (StructureTemplateEntity temp : result) {
				if (temp.getName().equals(structureTemplateName)) {
					template = temp;
					break;
				}
			}
			
			if (template == null) {
				return Messages.get("structureTemplate.not_found",
						structureTemplateName);
			}
			
			return getBusiness().getPageBusiness()
				.createStructuredPageRenderDecorator(page, 
						getBusiness().getLanguage(),
						template).getContent();
		}
		return Messages.get("approved_content_not_found");
	}

	@Override
	public PicasaVelocityService getPicasa() {
		if (picasaVelocityService == null) {
			picasaVelocityService = new PicasaVelocityServiceImpl(getBusiness());
		}
		return picasaVelocityService;
	}

	@Override
	public List<PageEntity> findPageChildren(String path, Date startDate,
			Date endDate) {
		return getBusiness().getPageBusiness().getByParentApproved(path, startDate, 
				endDate);
	}

	@Override
	public List<PageEntity> findPageChildrenMonth(String path, int year,
			int month) {
		try {
			Date startDate = DateUtil.toDate(String.format("01.%02d.%d", 
					month, year));
			Date endDate = DateUtil.toDate(String.format("01.%02d.%d", month + 1, 					
					year));
			return findPageChildren(path, startDate, endDate);
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			return Collections.EMPTY_LIST;
		}
	}

	@Override
	public List<PageEntity> findPageChildren(List<String> paths) {
		List<PageEntity> result = new ArrayList<PageEntity>();
		for (String path: paths) {
			result.addAll(findPageChildren(path));
		}	
		Collections.sort(result, PageHelper.PUBLISH_DATE);
		return result;
	}

	@Override
	public List<PageEntity> findPageChildren(List<String> paths, int start,
			int count) {
		return ListUtil.slice(findPageChildren(paths), start, count);
	}

	@Override
	public List<PageEntity> findPageChildrenMonth(List<String> paths, int year,
			int month) {
		List<PageEntity> result = new ArrayList<PageEntity>();
		for (String path: paths) {
			result.addAll(findPageChildrenMonth(path, year, month));
		}
		Collections.sort(result, PageHelper.PUBLISH_DATE);
		return result;
	}

	@Override
	public List<FileVO> getPageResources(String url) {
		return getResources("/page" + url);
	}

	@Override
	public List<FileVO> getResources(String path) {
		FolderEntity folder = getBusiness().getFolderBusiness().getByPath(path);
		if (folder == null) {
			return Collections.EMPTY_LIST;
		}
		List<FileVO> result = new ArrayList<FileVO>();
		for (FileEntity file : getDao().getFileDao().getByFolder(
				folder.getId())) {
			result.add(VosaoContext.getInstance().getBackService()
					.getFileService().getFile(file.getId()));
		}
		return result;
	}

	@Override
	public FileVO getResource(String path) {
		return getResource(path, "UTF-8");
	}

	@Override
	public FileVO getResource(String path, String encoding) {
		FileEntity file = getBusiness().getFileBusiness().findFile(path);
		if (file == null) return null;
		return VosaoContext.getInstance().getBackService().getFileService()
				.getFile(file.getId(), encoding);
	}

	@Override
	public List<CommentVO> getRecentComments(int limit) {
		// TODO add security check
		return CommentVO.create(getDao().getCommentDao().getRecent(limit));
	}
	
	@Override
	public List<PageEntity> searchFilter(List<String> sections, String query, int start, 
			int count) {
		
		logger.info("into searchFilter");
		String language = getBusiness().getLanguage();
		SearchResultFilter filter = null;
		if (sections != null) filter = new SectionSearchFilter(sections);
		
		List<PageEntity> pages = getBusiness().getSearchEngine().search(
				filter,
				query.toLowerCase(), 
				start, 
				count, 
				language);
		
		logger.info("out of searchFilter");
		return pages;
	}

	
}
