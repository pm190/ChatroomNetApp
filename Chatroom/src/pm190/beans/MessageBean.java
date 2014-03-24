package pm190.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * 
 * @author Patrick Mackinder
 */
@ManagedBean
@SessionScoped
public class MessageBean
{
	private String message;

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
}
