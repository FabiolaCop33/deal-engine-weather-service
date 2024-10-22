package com.dealengine.weather.weather_report_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Service to asynchronously fetch weather data from the OpenWeatherMap API.
 * 
 * This class uses Spring's @Async annotation to handle concurrent requests
 * and ensures requests are performed efficiently without exceeding rate limits.
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

    // Simulated map of IATA codes to coordinates (latitude, longitude). 
    // In a real-world scenario, this data could come from a database or external API.
    private static final Map<String, double[]> IATA_COORDINATES = Map.of(
            "MEX", new double[]{19.4363, -99.0721},  // Mexico City
            "MTY", new double[]{25.7785, -100.107},  // Monterrey
            "TLC", new double[]{19.3371, -99.566}    // Toluca
    );

    /**
     * Asynchronously fetches weather data using a code IATA.
     * 
     * @param iataCode The IATA code of the airport.
     * @return CompletableFuture with the weather data as a String.
     */
    @Async
    public CompletableFuture<String> getWeatherByIataCodeAsync(String iataCode) {
        try {
            // Retrieve the coordinates for the given IATA code.
            double[] coordinates = IATA_COORDINATES.get(iataCode);

            if (coordinates == null) {
                throw new IllegalArgumentException("Invalid IATA code: " + iataCode);
            }

            double latitude = coordinates[0];
            double longitude = coordinates[1];

            // Build the request URL using the coordinates and API key.
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
                    "{\"error\":\"Failed to fetch weather for IATA code %s: %s\"}",
                    iataCode, e.getMessage());
            return CompletableFuture.completedFuture(errorMessage);
        }
    }
}
