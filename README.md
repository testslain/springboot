# Stock Management System

This is a complete Spring Boot application for stock management. The application provides CRUD operations for products and requires admin authentication.

## Features

- Admin authentication system
- Product management (add, edit, update, delete)
- Responsive UI with Bootstrap and custom blue theme
- MySQL database integration

## Prerequisites

- Java 21
- Maven
- MySQL

## Setup

1. Create a MySQL database named `stock_management`
2. Update the database connection settings in `src/main/resources/application.properties` if needed
3. Run the application using: `mvn spring-boot:run`
4. Access the application at: `http://localhost:8080`

## Default Admin Credentials

- Username: admin
- Password: admin123

## Project Structure

- `src/main/java/com/stockmanagement`: Java source code
- `src/main/resources/templates`: Thymeleaf templates (HTML)
- `src/main/resources/static`: Static resources (CSS, JS)
