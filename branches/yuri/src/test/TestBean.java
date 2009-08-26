package test;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.User;


public class TestBean {

	private String text;
	private int counter;
	
	public void init() {
		text = "This is bean message";
	}

	public void changeText() {
		counter++;
		text = "message changed by JSF action " + counter;
		PersistenceManager pm = PMF.get().getPersistenceManager();
	    String query = "select from " + Greeting.class.getName();
	    List<Greeting> greetings = (List<Greeting>) pm.newQuery(query).execute();
	    text="You have " + greetings.size() +" records!";
	    pm.close();
	}
	
	public void saveRecord() {
		
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        String content = "Record #"+counter++;;
        Date date = new Date();
        Greeting greeting = new Greeting(user, content, date);
        Greeting greeting2 = new Greeting(user, "double", date);

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(greeting);
            greeting.setContent("changed");
            pm.makePersistent(greeting2);
         //   pm.deletePersistent(greeting);
        } finally {
            pm.close();
        }
       
        
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
