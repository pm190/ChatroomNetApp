package pm190.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

/**
 * 
 * @author Patrick Mackinder
 */
@ManagedBean
@SessionScoped
public class MessageBean
{
	private String message;
	private String messageOutput;

	public MessageBean()
	{
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getMessageOutput()
	{
		return messageOutput;
	}
	
	public void send() 
	{
		messageOutput = "User 1: " + message;
		message = "";
	}
}
