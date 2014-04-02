package pm190.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * 
 * @author Patrick Mackinder
 */
@ManagedBean
@SessionScoped
public class TestBean
{
	private int counter;
	private List<String> numbers = new ArrayList<String>();
	private int tabIndex;
	
	public TestBean()
	{
		Thread t = new Thread(new TestChatter(this));
		t.setDaemon(true);
		t.start();
	}
	
	public int getCounter()
	{
		return counter;
	}

	public void setCounter(int counter)
	{
		this.counter = counter;
	}
	
	public List<String> getNumbers()
	{
		return numbers;
	}

	public int getTabIndex()
	{
		return tabIndex;
	}

	public void setTabIndex(int tabIndex)
	{
		this.tabIndex = tabIndex;
	}

	public void increment()
	{
		
		System.out.println(++counter);
		numbers.add(""+counter);
	}
}
