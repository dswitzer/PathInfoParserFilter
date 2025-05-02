# My Tomcat Filter

This project is a Java Maven application that implements a custom filter for Tomcat 11. The filter is designed to intercept requests and responses in a web application.

## Project Structure

```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── MyFilter.java
│   │   └── resources
│   │       └── META-INF
│   │           └── context.xml
│   └── test
│       ├── java
│       │   └── com
│       │       └── example
│       │           └── MyFilterTest.java
│       └── resources
├── pom.xml
└── README.md
```

## Prerequisites

- Java 11
- Maven 3.6 or higher
- Apache Tomcat 11

## Building the Project

To build the project, navigate to the project directory and run:

```
mvn clean install
```

This command will compile the source code, run tests, and package the application.

## Running the Filter

To run the filter, deploy the generated WAR file to your Tomcat 11 server. Ensure that the `context.xml` file is correctly configured to define the filter and its mapping.

## Testing the Filter

Unit tests for the filter are located in the `src/test/java/com/example/MyFilterTest.java` file. You can run the tests using:

```
mvn test
```

This command will execute the tests and provide feedback on their success or failure.

## License

This project is licensed under the MIT License. See the LICENSE file for more details.