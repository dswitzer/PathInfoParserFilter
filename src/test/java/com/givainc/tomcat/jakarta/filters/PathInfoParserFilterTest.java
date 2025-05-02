package com.givainc.tomcat.jakarta.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class PathInfoParserFilterTest {

	private PathInfoParserFilter pathInfoFilter;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private FilterConfig config;
	private FilterChain chain;

	@BeforeEach
	public void setUp() {
		pathInfoFilter = new PathInfoParserFilter();
		
		// create our mock objects
		request = Mockito.mock(HttpServletRequest.class);
		response = Mockito.mock(HttpServletResponse.class);
		config = Mockito.mock(FilterConfig.class);
		chain = Mockito.mock(FilterChain.class);

		Mockito.when(request.getContextPath()).thenReturn("");
		
		RequestDispatcher dispatcher = Mockito.mock(RequestDispatcher.class);
		Mockito.when(request.getRequestDispatcher(Mockito.anyString())).thenReturn(dispatcher);
	}

	@Test
	public void shouldParseUriWithPathInfoWhenDefaultPattern() throws IOException, ServletException {
		// we have an empty config, so we should use our default regex
		pathInfoFilter.init(config);

		Mockito.when(request.getRequestURI()).thenReturn("/index.cfm/2/my/fake/path");
		Mockito.when(request.getServletPath()).thenReturn("/index.cfm");
		
		pathInfoFilter.doFilter(request, response, chain);

		// we should intercept our request and forward it to a new HttpServletRequestWrapper
		verify(request).getRequestDispatcher("/index.cfm");
		verify(request.getRequestDispatcher("/index.cfm")).forward(Mockito.argThat(arg -> arg instanceof jakarta.servlet.http.HttpServletRequestWrapper), Mockito.eq(response));
	}

	@Test
	public void shouldNotParseUriWithDefaultPatternWhenNoPathInfo() throws IOException, ServletException {
		// we have an empty config, so we should use our default regex
		pathInfoFilter.init(config);

		Mockito.when(request.getRequestURI()).thenReturn("/index.cfm");
		Mockito.when(request.getServletPath()).thenReturn("/index.cfm");
		
		pathInfoFilter.doFilter(request, response, chain);

		// since our pattern does not match, we should not have called our dispatcher
		verifyNoInteractions(request.getRequestDispatcher("/index.cfm"));
	}

	@Test
	public void shouldNotParseUriWithDefaultPatternWhenPatternDoesNotMatch() throws IOException, ServletException {
		// we have an empty config, so we should use our default regex
		pathInfoFilter.init(config);

		Mockito.when(request.getRequestURI()).thenReturn("/index.htm/2/my/fake/path");
		Mockito.when(request.getServletPath()).thenReturn("/index.htm");
		
		pathInfoFilter.doFilter(request, response, chain);

		// since our pattern does not match, we should not have called our dispatcher
		verifyNoInteractions(request.getRequestDispatcher("/index.htm"));
	}


	@Test
	public void shouldParseUriWithPathInfoWhenCustomPattern() throws IOException, ServletException {
		// define our pattern in the config object 
		Mockito.when(config.getInitParameter("pattern")).thenReturn("^(/.+?\\.jsp)(/.*)");

		// we have an empty config, so we should use our default regex
		pathInfoFilter.init(config);

		Mockito.when(request.getRequestURI()).thenReturn("/index.jsp/2/my/fake/path");
		Mockito.when(request.getServletPath()).thenReturn("/index.jsp");
		
		pathInfoFilter.doFilter(request, response, chain);

		// we should intercept our request and forward it to a new HttpServletRequestWrapper
		verify(request).getRequestDispatcher("/index.jsp");
		verify(request.getRequestDispatcher("/index.jsp")).forward(Mockito.argThat(arg -> arg instanceof jakarta.servlet.http.HttpServletRequestWrapper), Mockito.eq(response));
	}

	@Test
	public void shouldNotParseUriWithCustomPatternWhenNoPathInfo() throws IOException, ServletException {
		// define our pattern in the config object 
		Mockito.when(config.getInitParameter("pattern")).thenReturn("^(/.+?\\.jsp)(/.*)");

		// we have an empty config, so we should use our default regex
		pathInfoFilter.init(config);

		Mockito.when(request.getRequestURI()).thenReturn("/index.jsp");
		Mockito.when(request.getServletPath()).thenReturn("/index.jsp");
		
		pathInfoFilter.doFilter(request, response, chain);

		// since our pattern does not match, we should not have called our dispatcher
		verifyNoInteractions(request.getRequestDispatcher("/index.jsp"));
	}

	@Test
	public void shouldNotParseUriWithCustomPatternWhenPatternDoesNotMatch() throws IOException, ServletException {
		// define our pattern in the config object 
		Mockito.when(config.getInitParameter("pattern")).thenReturn("^(/.+?\\.jsp)(/.*)");

		// we have an empty config, so we should use our default regex
		pathInfoFilter.init(config);

		Mockito.when(request.getRequestURI()).thenReturn("/index.htm/2/my/fake/path");
		Mockito.when(request.getServletPath()).thenReturn("/index.htm");
		
		pathInfoFilter.doFilter(request, response, chain);

		// since our pattern does not match, we should not have called our dispatcher
		verifyNoInteractions(request.getRequestDispatcher("/index.htm"));
	}
}