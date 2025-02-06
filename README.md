# Tandem - Box Club Management System 🥊
Welcome to the Box Club Management System! This project is designed to simplify and enhance the management of boxing club activities.

## 📋 Table of Contents
- [Technologies Used](#technologies-used)
- [Domain Model](#domain-model)
- [Project Structure📁](#project-structure)
- [Setup and Running the Project 🚀](#setup-and-running-the-project)
- [Additional Information](#additional-information)
  - [Running Tests](#running-tests)
  - [Known Issues](#known-issues)

## Technologies Used 

<details>
<summary>Show technologies 💡</summary>
  
- Spring Boot
- Spring Security
- Spring Web
- Spring Data JPA
- Spring Security OAuth2 (for Google Authentication)
- Maven
- Docker
-	Lombok
-	MySQL
- Redis
-	Liquibase
-	Mapstruct
- Swagger
</details>

## Domain Model
+ **User**: Represents a registered user, including attributes like name, email, password, and role .
 
+ **Message**: Stores questions or feedback submitted by users.
 
+ **Question**: Contains a list of frequently asked questions (FAQs).

## Project Structure📁
```plaintext
src/main/java/box/app
├── config
├── controller
├── dto
├── exception
├── mapper
├── model
├── repository
├── security
├── service
└── validation

src/main/resources
├── db.changelog
 ├──changes
 └──db.changelog-master.yaml
├── application.properties
└── liquibase.properties

src/test/java/box/app
├── config
├── controller
├── repository
└── service

src/test/resources
├── database
└── application.properties
```

# Setup and Running the Project🚀
1. Clone the repository to your computer.
2. Open the project in IntelliJ IDEA or another preferred IDE.
3. Use Maven to build the project.
4. Database Setup:

Open the application.properties file in the root directory of the project.
```plaintext
//Replace with your own database settings
 spring.datasource.url=jdbc:mysql://localhost:3306/database_name
 spring.datasource.username=your_name
 spring.datasource.password=your_password
 spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```
Ensure that a MySQL database is created with the specified database name in the configuration file.

<details>
<summary>🔑 Google Authentication Setup</summary>

**Obtain API Credentials**:
- Go to the [Google Cloud Console](https://console.cloud.google.com/).
- Create a new project or select an existing one.
- Navigate to **APIs & Services** → **Credentials**.
- Create an **OAuth 2.0 Client ID**.
- Configure the redirect URI (e.g., `http://localhost:8080/login/oauth2/code/google`).

**Edit application.properties**:
```properties
spring.security.oauth2.client.registration.google.client-id=client.registration_google_client_id
spring.security.oauth2.client.registration.google.client-secret=client.registration_google_client_secret
spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8080/login/oauth2/code/google
spring.security.oauth2.client.registration.google.scope=profile, email
spring.security.oauth2.client.registration.google.client-name=Google

```
**Edit Redis Configuration:**
```plaintext
spring.data.redis.host=redis_host
spring.data.redis.port=redis_port
spring.data.redis.password=redis_password
spring.data.redis.ssl.enabled=true
```
Ensure that a Redis database is created with the specified database name in the configuration file.
</details>
5. Run the application.

## Additional Information
### API Documentation
📖 This project uses Swagger for API documentation. Access the documentation [here](http://ec2-54-196-53-183.compute-1.amazonaws.com/swagger-ui/index.html#/
).

**You can view the endpoints and test the application.**
### Docker 🐳
The project is Dockerized for easy deployment. Build the Docker container using the following commands:
```plaintext
 docker build -t box-club-app .
 docker run -p 8081:8080 box-club-app
```
### Running Tests
Ensure that the project is built and use Maven to run the tests:
```plaintext
 mvn test
```

## Known Issues 
🔒 **Ensure all sensitive information is stored in environment variables and not committed to version control.**
- For security reasons, it is important to store sensitive information, such as database credentials, API keys, and other secrets, in environment variables.
- This project provides an example .env.sample file that contains placeholders for the necessary variables.

<details>
<summary>Steps to Configure .env:</summary>
  
1. Copy the .env-sample file to .env:
```plaintext
cp .env-sample .env
```
2. Edit the .env file to include your own configuration
</details>

*❗By using this .env file, you ensure that all sensitive data is managed securely and separately from your codebase. Make sure to add .env to your .gitignore file to avoid accidentally committing it to version control.❗*

**I advise paying close attention to this aspect.👀**
