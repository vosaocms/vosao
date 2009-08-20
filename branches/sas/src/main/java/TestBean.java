import java.io.Serializable;
import java.util.List;

import org.vosao.dao.PageDao;
import org.vosao.entity.PageEntity;


public class TestBean implements Serializable {

	private String text;
	private int counter;
	private List<PageEntity> pages;
	
	private PageDao pageDao;
	
	public void init() {
		text = "This is bean message";
		pages = pageDao.select();
	}

	public void changeText() {
		counter++;
		text = "message changed by JSF action " + counter;
	}
	
	public void addPage() {
		PageEntity page = new PageEntity("Page title " + counter,"page content",
				"/page/" + counter, null);
		pageDao.save(page);
		pages.add(page);
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public PageDao getPageDao() {
		return pageDao;
	}

	public void setPageDao(PageDao pageDao) {
		this.pageDao = pageDao;
	}

	public List<PageEntity> getPages() {
		return pages;
	}
	
}
