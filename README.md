# zpp
Demo web application for integrating currency exchange rates API

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
-MongoDB for backend (user storage)
-Hibernate validation - JSR-303 validators for forms
-Spring Rest template - for accessing APIs
-Jackson - JSON to POJO mapping and conversion

External APIs:
-Currencylayer.com REST API 
-Openexchange.org REST API


