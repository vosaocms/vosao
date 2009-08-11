
public class TestBean {

	private String text;
	
	public void init() {
		text = "This is bean message";
	}

	public void changeText() {
		text = "message changed by JSF action";
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
