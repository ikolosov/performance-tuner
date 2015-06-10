### Performance Tuner
This application provides a live example of asynchronous HTTP request processing with Java EE 7 Servlets.
The vivid example of performance benefits that async processing grants for a long-running HTTP requests.

The app is designed to make comparison between regular blocking request processing and non-blocking async one possible.
Concurrent HTTP request generation is backed by Apache HttpClient lib.

### Technology Stack
* JDK 8
* Apache Maven v.3.2
* Java EE 7.0 (Servlets, ManagedExecutorService, EJB)
* Apache HttpComponents v.4.5
* WildFly Application Server v.8.2

### Build Instructions
Invoke the following maven command from the app root dir:

`mvn clean package`

Examine build log, make sure build was successful:

`[INFO] BUILD SUCCESS`

### Launch Instructions
web-application

`Once the app is assembled, deploy web-application.war to WildFly server and make sure it's accessible in web browser.`

requester

`Take a look at AtWork class - this is the entry point`