# My Plant Store
This is a Spring Boot application. This projects goal is to manage data in small plant store.
- You can manage your customers, products and orders.
- There are three categories(plants, pots and soil), so you can add new product in one of them with the following details: for plant - price, 
for pot - price, quantity and dimensions, for soil - price and volume.
- You can record a new order, and based on information about soil type, pot type and plant the app calculate the final plant cost.
- You can view sales report for different period by date and by product - total gross and net sales, average gross and net sales per day, month.

## Built With
Eclipse

## Tools used in this project:

- Spring Boot 
- Maven
- Java 
- MySQL 
- Spring Data JPA
- Bootstrap
- Thymeleaf

## Requirements:
To run this project, you'll need to have a basic environment to run a Java, MySQL App.

## How to Run this application
- Clone the Repository
- Change the src/main/resources/application.properties with your MySQL instance properties
- In MYSQL create a new schema named "plantstore" or import src/main/resources/database/plant_store.sql
- Run the project
- Navigate to http://localhost:8080/MyStore 
