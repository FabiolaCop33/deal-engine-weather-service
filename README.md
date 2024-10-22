# Weather Report API - Backend

This is the backend service for the Weather Report application. It provides weather reports for flights, including the weather conditions of origin and destination airports. The service uses **OpenWeatherMap API** and demonstrates concurrency, caching, and error handling.

## Features
- **Weather Reports**: Fetches real-time weather data from OpenWeatherMap.
- **Concurrency**: Processes requests asynchronously to improve performance.
- **Error Handling**: Handles API errors gracefully.
- **Caching**: In-memory caching to reduce redundant API calls.
- **H2 Database**: Embedded database for testing purposes.
- **RESTful API**: Designed with Spring Boot for easy integration with other services.

## Requirements
- **Java 17** or higher
- **Maven** (for dependency management)
- An **OpenWeatherMap API key**
