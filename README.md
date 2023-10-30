#### How to build and run the application

Execute the following commands in the project's root directory (\api)

## Build the App
To build this app<br/>

    ./mvnw clean install (./mvnw or mvnw depending on your OS)

## Execute the App
To execute this app<br/>

    mvn clean install
  
    docker-compose build
  
    docker-compose up

## Login to the App
To login<br/>

    username = zrael
    password = $Password123

## API Doc
To access the api swagger documentation<br/>

    The swagger documentation can be found at http://localhost:8081/api/v1/swagger-ui.html

## Brief Architecture
- `LANGUAGE`: Java 17


- `FRAMEWORK`: Spring Boot 3


- `Application Server` : Embedded Apache Tomcat Server


- `DATABASE`: H2 in memory file database (SQL implementation)


- `ORM`: Hibernate with Spring Data Jpa Implementation


- `AUTHENTICATION`: Jwt
    - 1 day access token
    - 7 day refresh token


- `TEST FRAMEWORK`:
    - Junit
    - Mockito


- `BUILD TOOL`: Maven


- `CONTAINER VIRTUALIZATION`: Docker (Docker compose for config)
---