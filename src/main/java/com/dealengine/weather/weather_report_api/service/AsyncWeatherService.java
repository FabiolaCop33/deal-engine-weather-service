package com.dealengine.weather.weather_report_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Service to asynchronously fetch weather data using OpenWeatherMap API.
 * Uses Geocoding API to resolve IATA codes to city names and coordinates.
 */
@Service
public class AsyncWeatherService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${weather.api.key}")
    private String apiKey;

    private static final String WEATHER_API_URL =
            "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={apiKey}&units=metric";

    private static final String GEOCODING_API_URL =
            "http://api.openweathermap.org/geo/1.0/direct?q={cityName},{countryCode}&limit=1&appid={apiKey}";

    /**
     * Asynchronously fetches simplified weather data for a given IATA code (city/airport code).
     * 
     * @param cityCode IATA code of the city/airport.
     * @param countryCode ISO 3166-1 alpha-2 country code.
     * @return CompletableFuture with simplified weather data as a Map.
     */
    @Async
    public CompletableFuture<Map<String, Object>> getSimplifiedWeatherAsync(String cityCode, String countryCode) {
        try {
            // Resolve the city name and coordinates using the Geocoding API.
            String geoUrl = GEOCODING_API_URL
                    .replace("{cityName}", cityCode)
                    .replace("{countryCode}", countryCode)
                    .replace("{apiKey}", apiKey);

            Map<String, Object>[] geoData = restTemplate.getForObject(geoUrl, Map[].class);

            if (geoData == null || geoData.length == 0) {
                return CompletableFuture.completedFuture(Map.of(
                        "city", cityCode,
                        "error", "City not found",
                        "message", "The provided IATA code is not recognized by the API"
                ));
            }

            Map<String, Object> location = geoData[0];
            double lat = (double) location.get("lat");
            double lon = (double) location.get("lon");

            // Fetch weather data using the resolved coordinates.
            String weatherUrl = WEATHER_API_URL
                    .replace("{lat}", String.valueOf(lat))
                    .replace("{lon}", String.valueOf(lon))
                    .replace("{apiKey}", apiKey);

            Map<String, Object> weatherData = restTemplate.getForObject(weatherUrl, Map.class);

            if (weatherData != null && weatherData.containsKey("main")) {
                String city = (String) location.get("name");
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
                        "city", cityCode,
                        "error", "Weather data not available"
                ));
            }

        } catch (Exception e) {
            return CompletableFuture.completedFuture(Map.of(
                    "city", cityCode,
                    "error", "Failed to fetch data",
                    "message", e.getMessage()
            ));
        }
    }
}
