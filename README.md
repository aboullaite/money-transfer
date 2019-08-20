# Simple RESTful API for money transfers between accounts.
### stack
- Java 8
- [Maven](https://maven.apache.org/)
- [Spark Framework](http://sparkjava.com) (with embedded Jetty)
- [SLF4j](https://www.slf4j.org/)
- [google/gson](https://github.com/google/gson)
- [JUnit 5](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
- [rest-assured](http://rest-assured.io/)

### Run the JAR
The project generates dummy data for testing. This is completely optional and can be ignored during the build

+ Run with data: `java -jar target/money-transfer-1.0-SNAPSHOT.jar`
+ Run without data: `java -jar target/money-transfer-1.0-SNAPSHOT.jar NO_DATA`

### Run with Docker
The project comes with a Dockerfile to build docker image for the app. As mentionned previously, we can ca choose between generating ot not the data:

+ Build image with data: `docker build -t rev/money-transfer .`
+ Build image without data: `docker build --build-arg arg=No_DATA -t rev/ev/money-transfer .`

and finally, simply run the image `docker run -p 8080:9999 rev/money  `