package com.givainc.tomcat.jakarta.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PathInfoParserFilter implements Filter {
	private static String pattern;
	private static PathInfoParser parser;
	private static final String REGEX_DEFAULT = "^(/.+?\\.cf[cm])(/.*)";
	private static final Logger logger = Logger.getLogger(PathInfoParserFilter.class.getName());

	@Override
	public void init(FilterConfig config) throws ServletException {
		configure(config.getInitParameter("pattern"));
	}

	public PathInfoParserFilter configure(){
		return configure(null);
	}

	public PathInfoParserFilter configure(String regexPattern){
		pattern = (regexPattern != null ? regexPattern : REGEX_DEFAULT);
		parser = new PathInfoParser(pattern);

		return this;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final String requestUri = ((HttpServletRequest) request).getRequestURI();

		final PathInfoParserResult results = parser.parse(requestUri, ((HttpServletRequest) request).getContextPath());

		if( results != null ){
			final String processedUri = results.getRequestUri();
			final String pathInfo = results.getPageInfo();

			logger.log(Level.FINE, requestUri + " matches " + pattern + " pattern, now: " + processedUri + " path_info: " + pathInfo);

			final HttpServletRequestWrapper wrapped = new HttpServletRequestWrapper((HttpServletRequest) request) {
					@Override
					public String getPathInfo() {
							return pathInfo;
					}
			};

			request.getRequestDispatcher(processedUri).forward(wrapped, response);
		} else {
			// Continue the filter chain
			chain.doFilter(request, response); 
		}
	}

	@Override
	public void destroy() {
		// clean up any objects that need closed/finalized
	}
}