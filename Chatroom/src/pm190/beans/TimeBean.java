package pm190.beans;

import java.util.Calendar;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 * 
 * @author Patrick Mackinder
 */
@ManagedBean
@ApplicationScoped
public class TimeBean
{
	private Calendar time = Calendar.getInstance();
	
	public TimeBean()
	{
	}

	public String getTime()
	{
		return String.format("Time: %2d:%02d", time.get(Calendar.MINUTE), time.get(Calendar.SECOND));
	}
}
