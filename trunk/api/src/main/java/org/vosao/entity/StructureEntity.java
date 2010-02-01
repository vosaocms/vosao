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

package org.vosao.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.vo.StructureFieldVO;

import com.google.appengine.api.datastore.Text;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class StructureEntity implements BaseEntity {

	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(StructureEntity.class);

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;
	
	@Persistent
	String title;
	
	@Persistent(defaultFetchGroup = "true")
	private Text content;
	
	public StructureEntity() {
	}
	
	public StructureEntity(String title, String content) {
		this();
		this.title = title;
		this.content = new Text(content);
	}
	
	@Override
	public Object getEntityId() {
		return id;
	}

	public void copy(final StructureEntity entity) {
		setTitle(entity.getTitle());
		setContent(entity.getContent());
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		if (content == null) {
			return null;
		}
		return content.getValue();
	}
	
	public void setContent(String content) {
		this.content = new Text(content);
	}

	public boolean equals(Object object) {
		if (object instanceof StructureEntity) {
			StructureEntity entity = (StructureEntity)object;
			if (getId() == null && entity.getId() == null) {
				return true;
			}
			if (getId() != null && getId().equals(entity.getId())) {
				return true;
			}
		}
		return false;
	}

	public List<StructureFieldVO> getFields() {
		try {
			Document doc = DocumentHelper.parseText(getContent());
			List<StructureFieldVO> result = new ArrayList<StructureFieldVO>();
			for (Iterator<Element> i = doc.getRootElement().elementIterator(); 
					i.hasNext(); ) {
					Element element = i.next();
					if (element.getName().equals("field")) {
						result.add(new StructureFieldVO(
								element.elementText("title"),
								element.elementText("name"),
								element.elementText("type")));
					}
			}
			return result;
		} catch (DocumentException e) {
			logger.error(e.getMessage());
			return Collections.EMPTY_LIST;
		}
	}
}
