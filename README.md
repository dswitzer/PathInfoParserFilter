# PathInfoParserFilter

This is a filter for Tomcat 10+ (Jakarta-based classes) which implements a way to have Tomcat
monitoring Request URIs that contains "path info" and pass it along to one of your existing
servlets.

This project was inspired by [RegexPathInfoFilter](https://github.com/cfmlprojects/regexpathinfofilter)
but re-written from scratch to support Tomcat 10+ and contains unit test coverage for the filter.


## Prerequisites

- Java 11+
- Maven 3.6 or higher
- Apache Tomcat 10+


## Building the Project

To build the project, navigate to the project directory and run:

```
mvn clean install
```
This command will compile the source code, run tests, and package the application.


## Testing the Filter

Unit tests for the filter are located in the `src/test/java/com/givainc/tomcat/jakarta/filters/` folder. You can run the tests using:

```
mvn test
```
This command will execute the tests and provide feedback on their success or failure.


## Applying the Filter

To use the filter in your Tomcat deployment:

1. Copy the compiled JAR file to the `lib` directory of your web application (`WEB-INF/lib/`) or to Tomcat's shared library folder (`$CATALINA_HOME/lib/`) (or any custom JAR folder you have configured for Tomcat to load classes from).

2. Add the filter configuration to your `web.xml`, making sure the `pattern` matches your environmental needs:

```xml
<filter>
	<filter-name>PathInfoParserFilter</filter-name>
	<filter-class>com.givainc.tomcat.jakarta.filters.PathInfoParserFilter</filter-class>
	<init-param>
		<param-name>pattern</param-name>
		<!--
			Your pattern needs to return 2 group matches:

			Group 1: The Request URL portion you want to use in the request forward. This will 
			         be processed by other Tomcat servlets.
			Group 2: The information that should be returned as the "path info" for the request.
			
			The default pattern is shown below, so you could exclude it if you wanted any path
			with the ".cfm" or ".cfc" pattern in the Request URL to be handled by a Tomcat servlet.
		-->
		<param-value>^(/.+\.cf[cm])(/.*)</param-value>
	</init-param>        
</filter>
<filter-mapping>
	<filter-name>PathInfoParserFilter</filter-name>
	<url-pattern>/*</url-pattern>
	<dispatcher>REQUEST</dispatcher>
	<dispatcher>FORWARD</dispatcher>        
</filter-mapping>
```

3. If deploying multiple applications that need the filter, consider adding the configuration to Tomcat's `conf/web.xml` instead.

4. Restart your Tomcat server or redeploy your application for changes to take effect.

Note: When using the filter, ensure your servlet mappings are configured to handle the parsed path info correctly.


## License

This project is licensed under the Apache License 2 License. See the LICENSE file for more details.