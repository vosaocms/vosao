import java.io.Serializable;

import org.vosao.dao.PageDao;


public class TestBean implements Serializable {

	private String text;
	private int counter;
	
	private PageDao pageDao;
	
	public void init() {
		text = "This is bean message";
	}

	public void changeText() {
		counter++;
		text = "message changed by JSF action " + counter;
		pageDao.test();
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
	
}
