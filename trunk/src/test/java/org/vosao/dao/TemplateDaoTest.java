package org.vosao.dao;

import java.util.List;

import org.vosao.entity.TemplateEntity;

public class TemplateDaoTest extends AbstractDaoTest {

	private TemplateEntity addTemplate(final String title, final String content) {
		TemplateEntity Template = new TemplateEntity(title, content);
		getDao().getTemplateDao().save(Template);
		return Template;
	}
	
	public void testSave() {
		addTemplate("title", "content");
		List<TemplateEntity> Templates = getDao().getTemplateDao().select();
		assertEquals(1, Templates.size());
		TemplateEntity Template1 = Templates.get(0);
		assertEquals("title", Template1.getTitle());
	}	
	
	public void testGetById() {
		TemplateEntity Template = addTemplate("title","content");
		assertNotNull(Template.getId());
		TemplateEntity Template2 = getDao().getTemplateDao().getById(Template.getId());
		assertEquals(Template.getTitle(), Template2.getTitle());
		assertEquals(Template.getContent(), Template2.getContent());
	}	

	public void testSelect() {
		addTemplate("title1", "content1");
		addTemplate("title2", "content2");
		addTemplate("title3", "content3");
		List<TemplateEntity> Templates = getDao().getTemplateDao().select();
		assertEquals(3, Templates.size());
	}	
	
	public void testUpdate() {
		TemplateEntity Template = addTemplate("title1", "content1");
		assertNotNull(Template.getId());
		TemplateEntity Template2 = getDao().getTemplateDao().getById(Template.getId());
		Template2.setTitle("update");
		getDao().getTemplateDao().save(Template2);
		TemplateEntity Template3 = getDao().getTemplateDao().getById(Template.getId());
		assertEquals("update", Template3.getTitle());
	}
	
	public void testResultList() {
		addTemplate("title1", "content1");
		addTemplate("title2", "content2");
		addTemplate("title3", "content3");
		List<TemplateEntity> Templates = getDao().getTemplateDao().select();
		TemplateEntity Template = new TemplateEntity("title", "content");
		Templates.add(Template);
		assertEquals(4, Templates.size());
	}
	
}
