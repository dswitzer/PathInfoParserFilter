package com.givainc.tomcat.jakarta.filters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathInfoParser {
	final private Pattern regex;

	public PathInfoParser(String pattern){
		if( pattern == null ) throw new IllegalArgumentException("The pattern expression cannot be null!");

		this.regex = Pattern.compile(pattern);
	}

	public PathInfoParserResult parse(String uri){
		return parse(uri, null);
	}

	public PathInfoParserResult parse(String uri, String contextPath){
		final Matcher matcher = regex.matcher(uri);

		// when our regex matches the full URI, then we have information we can extract
		if( matcher.matches() ){
			if( matcher.groupCount() < 2 ){
				throw new IllegalStateException(
					"The pattern must define at least 2 capturing groups, but found " 
					+ matcher.groupCount()
					+ ". The first group should be the new URL path and the second group should be the page info."
				);
			}
			
			return new PathInfoParserResult(matcher.group(1), matcher.group(2), contextPath);
		}

		return null;
	}
}
