import java.io.Serializable;


public class TestBean implements Serializable {

	private String text;
	private int counter;
	
	public void init() {
		text = "This is bean message";
	}

	public void changeText() {
		counter++;
		text = "message changed by JSF action " + counter;
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
	
}
