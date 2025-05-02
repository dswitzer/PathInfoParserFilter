package com.givainc.tomcat.jakarta.filters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PathInfoParserTest {

	@Test
	public void shouldThrowExceptionWhenNullPattern() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			new PathInfoParser(null);
		});
		assertEquals("The pattern expression cannot be null!", exception.getMessage());
	}

	@Test
	public void shouldThrowExceptionWhenPatternDoesNotReturnAtLeast2Groups() {
		PathInfoParser parser = new PathInfoParser("^(/.+\\.cf[cm])/.*");
		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
			parser.parse("/index.cfm/some/path");
		});
		
		assertTrue(exception.getMessage().contains("The pattern must define at least 2 capturing groups"));
	}

	@Test
	public void shouldParseValidUri() {
		PathInfoParser parser = new PathInfoParser("^(/.+\\.cf[cm])(/.*)");
		PathInfoParserResult result = parser.parse("/index.cfm/some/path");

		assertNotNull(result);
		assertEquals("/index.cfm", result.getUriPath());
		assertEquals("/some/path", result.getPageInfo());
		assertNull(result.getContextPath());
	}

	@Test
	public void shouldParseHtmLikeUris() {
		PathInfoParser parser = new PathInfoParser("^(/.+\\.html?)(/.*)");
		PathInfoParserResult result = parser.parse("/index.htm/some/path");

		assertNotNull(result);
		assertEquals("/index.htm", result.getUriPath());
		assertEquals("/some/path", result.getPageInfo());
		assertNull(result.getContextPath());
	}

	@Test
	public void shouldParseHtmlLikeUris() {
		PathInfoParser parser = new PathInfoParser("^(/.+\\.html?)(/.*)");
		PathInfoParserResult result = parser.parse("/index.html/some/path");

		assertNotNull(result);
		assertEquals("/index.html", result.getUriPath());
		assertEquals("/some/path", result.getPageInfo());
		assertNull(result.getContextPath());
	}

	@Test
	public void shouldReturnNullForNonMatchingUri() {
		PathInfoParser parser = new PathInfoParser("^(/.+\\.cf[cm])(/.*)");
		PathInfoParserResult result = parser.parse("/nonmatching/path");

		assertNull(result);
	}

	@Test
	public void shouldReturnNullWhenPatternDoesNotMatchFullUri() {
		PathInfoParser parser = new PathInfoParser("^(/.+\\.cf[cm])");
		PathInfoParserResult result = parser.parse("/index.cfm/some/path");

		assertNull(result);
	}

	@Test
	public void shouldHandleContextPath() {
		PathInfoParser parser = new PathInfoParser("^(/.+\\.cf[cm])(/.*)");
		PathInfoParserResult result = parser.parse("/app/index.cfm/some/path", "/app");

		assertNotNull(result);
		assertEquals("/app/index.cfm", result.getUriPath());
		assertEquals("/some/path", result.getPageInfo());
		assertEquals("/app", result.getContextPath());
		assertEquals("/index.cfm", result.getRequestUri());
	}
}
