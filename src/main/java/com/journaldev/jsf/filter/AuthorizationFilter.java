package com.journaldev.jsf.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


//urlPatterns - Specify one or more URL patterns to which the filter applies

@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml" })
public class AuthorizationFilter implements Filter {

	public AuthorizationFilter() {
	}

	@Override
        //This method is called by the web container to indicate to a filter that it is being placed into service.
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
        //This method is called by the container each time a request/response pair is passed through 
        //the chain due to a client request for a resource at the end of the chain.
        //In the doFilter method we will redirect user to login page if he tries to access other page without logging in.
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {

			HttpServletRequest reqt = (HttpServletRequest) request; //interface to provide request information for HTTP servlets. 
			HttpServletResponse resp = (HttpServletResponse) response;
                        //Returns the current HttpSession associated with this request or, if there is no current session and create is true, returns a new session. 
			HttpSession ses = reqt.getSession(false); //parameter - create - true to create a new session for this request if necessary; 
                                                                                        //false to return null if there's no current session 
//getRequestURI - Returns the part of this request's URL from the protocol name up to the query string in the first line of the HTTP request. 
			String reqURI = reqt.getRequestURI();
			if (reqURI.indexOf("/login.xhtml") >= 0 || reqURI.indexOf("/register.xhtml") >= 0
					|| (ses != null && ses.getAttribute("username") != null)
					|| reqURI.indexOf("/") > 0
					|| reqURI.contains("javax.faces.resource"))
				chain.doFilter(request, response);
			else
				resp.sendRedirect(reqt.getContextPath() + "/login.xhtml");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
        //This method is called by the web container to indicate to a filter that it is being taken out of service.
	public void destroy() {

	}
}
