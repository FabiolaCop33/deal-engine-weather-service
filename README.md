# deal-engine-weather-service
Weather Report API for Deal Engine's Backend Challenge: REST API with Spring Boot to fetch and cache weather reports for flights.

## Description
This project provides a **REST API** to retrieve the weather for the origin and destination airports of flight tickets. It processes multiple tickets concurrently, caches results for performance, and handles potential API or network errors gracefully.

## Features
- **REST API** for fetching weather information.
- **Concurrent processing** to handle multiple airport requests.
- **In-memory cache** to avoid redundant API calls.
- **Error handling** to gracefully handle API failures.
- **H2 database** for storing airport and flight information temporarily.

## Technologies Used
- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Web** (for building RESTful APIs)
- **Spring Data JPA** (for database operations)
- **H2 Database** (in-memory database for testing)
- **Lombok** (to reduce boilerplate code)

## Requirements
- **Java Development Kit (JDK) 17+**
- **Maven 3.8+**
- **Internet connection** (to fetch weather data)
- **IDE (e.g., IntelliJ IDEA, Eclipse)** or any code editor

## Installation
1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/deal-engine-weather-service.git
   cd deal-engine-weather-service

2. Build the project:
   ```bash
   mvn clean install
3. Run the application:
4. Access the API:
