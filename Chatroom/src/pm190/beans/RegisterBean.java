package pm190.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * 
 * @author Patrick Mackinder
 */
@ManagedBean
@SessionScoped
public class RegisterBean
{
	private String username;
	private String password;
	private String confirmPassword;
	private String email;
	private String confirmEmail;
	private String dob;
	private String description;
	
	public RegisterBean()
	{
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getConfirmPassword()
	{
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword)
	{
		this.confirmPassword = confirmPassword;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getConfirmEmail()
	{
		return confirmEmail;
	}

	public void setConfirmEmail(String confirmEmail)
	{
		this.confirmEmail = confirmEmail;
	}

	public String getDob()
	{
		return dob;
	}

	public void setDob(String dob)
	{
		this.dob = dob;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
