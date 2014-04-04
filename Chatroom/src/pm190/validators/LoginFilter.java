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
 * LoginFilter that redirects direct access to chatroom.xhtml if not logged in
 * @author Patrick Mackinder
 */
public class LoginFilter implements Filter
{
	private FilterConfig config;

	/**
	 * Redirects user to home.xhtml if user not logged in
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
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
			//chain.doFilter(request, response);
		}
	}

	/**
	 * initialise config
	 * @param config
	 * @throws ServletException
	 */
	@Override
	public void init(FilterConfig config) throws ServletException
	{
		this.config = config;
	}

	/**
	 * Destroy filter
	 */
	@Override
	public void destroy()
	{
		config = null;
	}
}
