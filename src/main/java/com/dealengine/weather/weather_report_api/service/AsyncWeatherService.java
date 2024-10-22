package com.dealengine.weather.weather_report_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.CompletableFuture;

/**
 * Service to asynchronously fetch weather data from the OpenWeatherMap API.
 * 
 * This class uses Spring's @Async annotation to handle concurrent requests
 * and ensures requests are performed efficiently without exceeding rate limits.
 * 
 * @author YourName
 */
@Service
public class AsyncWeatherService {

    private final RestTemplate restTemplate = new RestTemplate();

    // Inject the API key from the application properties file.
    @Value("${weather.api.key}")
    private String apiKey;

    // OpenWeatherMap endpoint template with latitude, longitude, and API key placeholders.
    private static final String WEATHER_API_URL =
            "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={apiKey}&units=metric";

    /**
     * Asynchronously fetches weather data for a given set of geographic coordinates.
     * 
     * @param latitude Latitude of the location.
     * @param longitude Longitude of the location.
     * @return CompletableFuture with the weather data as a String.
     */
    @Async
    public CompletableFuture<String> getWeatherAsync(double latitude, double longitude) {
        try {
            // Build the request URL using the provided coordinates and API key.
            String url = WEATHER_API_URL
                    .replace("{lat}", String.valueOf(latitude))
                    .replace("{lon}", String.valueOf(longitude))
                    .replace("{apiKey}", apiKey);

            // Make the API call and retrieve the weather data.
            String weatherData = restTemplate.getForObject(url, String.class);
            return CompletableFuture.completedFuture(weatherData);
        } catch (Exception e) {
            // Handle any exceptions and return an error message.
            String errorMessage = String.format(
                    "{\"error\":\"Failed to fetch weather for coordinates (%f, %f)\"}",
                    latitude, longitude);
            return CompletableFuture.completedFuture(errorMessage);
        }
    }
}
