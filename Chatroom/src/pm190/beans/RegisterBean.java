package pm190.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Contains information about a user registration. This class is session scoped
 * and can be reused for each new registration
 * 
 * @author Patrick Mackinder
 */
@ManagedBean
@SessionScoped
public class RegisterBean
{
	private String username;
	private String password;
	private String email;
	private String dob;
	private String description;
	
	/**
	 * Create register bean, created on new session
	 */
	public RegisterBean()
	{
	}

	/**
	 * Gets current username
	 * @return username
	 */
	public String getUsername()
	{
		return username;
	}
	
	/**
	 * Set current username
	 * @param username
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * Get current password
	 * @return password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * Set current password
	 * @param password
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * Gets current email
	 * @return
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * Set current email
	 * @param email
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}

	/**
	 * Gets current date of birth
	 * @return
	 */
	public String getDob()
	{
		return dob;
	}
	
	/**
	 * Sets current date of birth
	 * @param dob
	 */
	public void setDob(String dob)
	{
		this.dob = dob;
	}

	/**
	 * Gets current user description
	 * @return
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Sets current user description
	 * @param description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
}
