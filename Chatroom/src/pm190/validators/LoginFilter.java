package pm190.validators;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pm190.beans.UserBean;

/**
 * 
 * @author Patrick Mackinder
 */
public class LoginFilter implements Filter
{
	private FilterConfig config;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		System.out.println("Filter");
		UserBean userBean = (UserBean) ((HttpServletRequest) request).getSession().getAttribute("userBean");
		if(userBean.isLoggedIn())
		{
			chain.doFilter(request, response);
		}
		else
		{
			String contextPath = ((HttpServletRequest) request).getContextPath();
			((HttpServletResponse) response).sendRedirect(contextPath + "/pages/home.xhtml");
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException
	{
		this.config = config;
	}

	@Override
	public void destroy()
	{
		config = null;
	}
}
