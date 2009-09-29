package org.vosao.service.impl.vo;

import java.util.ArrayList;
import java.util.List;

import org.vosao.entity.CommentEntity;
import org.vosao.utils.DateUtil;

/**
 * Value object to be returned from services.
 * @author Alexander Oleynik
 */
public class CommentVO {

    private String id;
	private String pageId;
	private String name;
	private String content;
	private String publishDate;
	private String publishDateTime;
	private boolean disabled;

	public CommentVO(final CommentEntity entity) {
		super();
		this.id = entity.getId();
		this.pageId = entity.getPageId();
		this.name = entity.getName();
		this.content = entity.getContent();
		this.publishDate = DateUtil.toString(entity.getPublishDate());
		this.publishDateTime = DateUtil.dateTimeToString(entity.getPublishDate());
		this.disabled = entity.isDisabled();
	}

	public static List<CommentVO> create(List<CommentEntity> list) {
		List<CommentVO> result = new ArrayList<CommentVO>();
		for (CommentEntity comment : list) {
			result.add(new CommentVO(comment));
		}
		return result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getPublishDateTime() {
		return publishDateTime;
	}

	public void setPublishDateTime(String publishDateTime) {
		this.publishDateTime = publishDateTime;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
}
