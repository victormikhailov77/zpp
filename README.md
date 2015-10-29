# zpp
Demo web application with login form for integrating currency exchange rates API

Requirements:
-Main page visible only to authorized users
-Not authorized users redirected to login form
-New users can be created via registration form (available for not authorized users)
-After successful login, user redirected to main page
-Main page shows currency exchange rates for some selected currency

Source code: Java 8
Build system: Maven 3.3
Frameworks and libs:
-Spring MVC
-Spring Boot
-Spring Security
-Spring Data MongoDB
-Thymeleaf as HTML template engine
-MongoDB for backend
-Hibernate validation - JSR-303 validators for forms
-Spring Rest template - for accessing APIs
-Jackson - JSON to POJO mapping and conversion

External APIs:
-Currencylayer.com REST API 
-Openexchange.org REST API

Use local database - uncomment line in application.properties
#spring.data.mongodb.uri = mongodb://localhost:27017/local

Use server database - uncomment line in application.properties (default)
spring.data.mongodb.uri = mongodb://user:passsword@ds045054.mongolab.com:45054/viper


To run it locally:
1.Get sources to local machine
git clone https://github.com/victormikhailov77/zpp.git
cd zpp

2. Optional - get MongoDB and install locally (current prod version 3.0.7)
https://www.mongodb.org/downloads#production
start mongodb server instance
mongod --dbpath=<any_empty_directory with write access>

3.build jar:
mvn clean install

4.execute:
java -jar target/zpp-1.0-SNAPSHOT.jar

application starts in console

Testing web UI behaviour

1.Non-authorized user should be redirected to login page

open url:
localhost:8080/
or
localhost:8080/currencyview

Expected: 
user redirected to login form:
http://localhost:8080/login

2.Login form doesn't allow user to supply empty credentials or non-existent user login name

Expected:
user stays on login page
http://localhost:8080/login

3.Login form prompts user to register
click register button

Expected: redirected to
http://localhost:8080/register

4.Registration form doesn't allow to submit empty fields, or some garbage
login - 6 to 30 characters
password, verify password - 8 to 30 characters
first name, last name - allowed only letters, space and dash characters, 1 to 60 chars
email - checked against regexp pattern, xxx@xxx
street - 2 to 50 characters
city - 2 to 50 characters
state/province - 2 to 50 characters
country - selected from dropdown list, sorted by country name (note: order is locale-specific!)
post code - 2 to  12 characters
telephone - international format , must start with '+' sign, only digits and spaces permitted

Expected: in case of validation error - user stays on the same page, until input is fixed
in case of successful validation - user redirected back to http://localhost:8080/login

5.Login form redirects to main page, if login successful
Login, using credentials, created on registration form

Expected:
User redirected to page 
http://localhost:8080/currencyview


6. Logoff from protected page
User clicks logoff button on currencyview

Expected: user logged off and redirected to login page:
http://localhost:8080/login




