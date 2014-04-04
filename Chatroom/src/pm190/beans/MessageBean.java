package pm190.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * A message bean represents one message sent by a client to the server
 * It is session scoped and is reused by calling reset
 * 
 * @author Patrick Mackinder
 */
@ManagedBean
@SessionScoped
public class MessageBean
{
	private String message;

	/**
	 * Create message bean, created on new session
	 */
	public MessageBean()
	{
	}

	/**
	 * Return the current message
	 * @return message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * Set the current message
	 * @param message
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	/**
	 * set the current message to empty string
	 */
	public void reset()
	{
		message = "";
	}
}
