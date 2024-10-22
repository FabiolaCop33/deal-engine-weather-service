package com.dealengine.weather.weather_report_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Service to asynchronously fetch weather data from the OpenWeatherMap API.
 */
@Service
public class AsyncWeatherService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${weather.api.key}")
    private String apiKey;

    private static final String WEATHER_API_URL =
            "https://api.openweathermap.org/data/2.5/weather?q={cityCode}&appid={apiKey}&units=metric";

    /**
     * Asynchronously fetches simplified weather data for a given city code (IATA).
     *
     * @param cityCode IATA code of the city.
     * @return CompletableFuture with simplified weather data as a Map.
     */
    @Async
    public CompletableFuture<Map<String, Object>> getSimplifiedWeatherAsync(String cityCode) {
        try {
            String url = WEATHER_API_URL
                    .replace("{cityCode}", cityCode)
                    .replace("{apiKey}", apiKey);

            Map<String, Object> weatherData = restTemplate.getForObject(url, Map.class);

            // Extraer los datos relevantes solo si la respuesta es válida.
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
                        "error", "City not found",
                        "message", "The city code provided is not recognized by the API"
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
