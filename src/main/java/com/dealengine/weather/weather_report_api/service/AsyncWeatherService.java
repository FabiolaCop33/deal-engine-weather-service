package com.dealengine.weather.weather_report_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Service to asynchronously fetch weather data using the OpenWeatherMap API.
 * Now includes Geocoding API integration to handle city code resolution.
 */
@Service
public class AsyncWeatherService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${weather.api.key}")
    private String apiKey;

    private static final String WEATHER_API_URL =
            "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={apiKey}&units=metric";

    private static final String GEOCODING_API_URL =
            "http://api.openweathermap.org/geo/1.0/direct?q={cityCode}&limit=1&appid={apiKey}";

    /**
     * Asynchronously fetches simplified weather data for a given city code (IATA).
     *
     * @param cityCode IATA code of the city.
     * @return CompletableFuture with simplified weather data as a Map.
     */
    @Async
    public CompletableFuture<Map<String, Object>> getSimplifiedWeatherAsync(String cityCode) {
        try {
            // Resolve city code to coordinates using the Geocoding API
            String geoUrl = GEOCODING_API_URL
                    .replace("{cityCode}", cityCode)
                    .replace("{apiKey}", apiKey);

            List<Map<String, Object>> geoData = restTemplate.getForObject(geoUrl, List.class);

            if (geoData == null || geoData.isEmpty()) {
                return CompletableFuture.completedFuture(Map.of(
                        "error", "City not found",
                        "message", "The city code provided is not recognized by the API"
                ));
            }

            Map<String, Object> location = geoData.get(0);
            double lat = (double) location.get("lat");
            double lon = (double) location.get("lon");

            // Fetch weather data using the resolved coordinates
            String weatherUrl = WEATHER_API_URL
                    .replace("{lat}", String.valueOf(lat))
                    .replace("{lon}", String.valueOf(lon))
                    .replace("{apiKey}", apiKey);

            Map<String, Object> weatherData = restTemplate.getForObject(weatherUrl, Map.class);

            if (weatherData != null && weatherData.containsKey("main")) {
                String city = (String) weatherData.get("name");
                Map<String, Object> main = (Map<String, Object>) weatherData.get("main");
                double temperature = (double) main.get("temp");

                Map<String, Object> weather = 
                        ((List<Map<String, Object>>) weatherData.get("weather")).get(0);
                String conditions = (String) weather.get("description");

                return CompletableFuture.completedFuture(Map.of(
                        "city", city,
                        "temperature", temperature,
                        "conditions", conditions
                ));
            } else {
                return CompletableFuture.completedFuture(Map.of(
                        "error", "Weather data not found",
                        "message", "Failed to fetch weather data for the given city"
                ));
            }
        } catch (Exception e) {
            return CompletableFuture.completedFuture(Map.of(
                    "error", "Failed to fetch weather for " + cityCode,
                    "message", e.getMessage()
            ));
        }
    }
}
