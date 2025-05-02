package com.givainc.tomcat.jakarta.filters;

public class PathInfoParserResult {
	private final String uriPath;
	private final String pageInfo;
	private final String contextPath;

	public PathInfoParserResult(String uriPath, String pageInfo, String contextPath){
		this.uriPath = uriPath;
		this.pageInfo = pageInfo;
		this.contextPath = contextPath;
	}

	public String getRequestUri() {
		return (contextPath == null) ? uriPath : uriPath.replaceFirst(contextPath, "");
	}

	public String getUriPath() {
		return uriPath;
	}

	public String getPageInfo() {
		return pageInfo;
	}

	public String getContextPath() {
		return contextPath;
	}
}