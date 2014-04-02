package pm190.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

/**
 * 
 * @author Patrick Mackinder
 */
public class TestChatter implements Runnable
{	
	private final TestBean bean;
	
	public TestChatter(TestBean bean)
	{
		this.bean = bean;
	}
	
	@Override
	public void run()
	{
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			while((br.readLine()) != null)
			{
				bean.increment();
				PushContext pushContext = PushContextFactory.getDefault().getPushContext();
				pushContext.push("/counter", "counter updated");
			}

		}
		catch(IOException io)
		{
			io.printStackTrace();
		}
	}
}
