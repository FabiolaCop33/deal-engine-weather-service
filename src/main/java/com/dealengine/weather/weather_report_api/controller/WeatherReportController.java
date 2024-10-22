package com.dealengine.weather.weather_report_api.controller;


import com.dealengine.weather.weather_report_api.service.AsyncWeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller to handle requests for weather reports.
 * Uses concurrent processing to fetch weather data for origin and destination cities.
 */
@RestController
@RequestMapping("/api/weather")
public class WeatherReportController {

    private final AsyncWeatherService asyncWeatherService;

    /**
     * Constructor for dependency injection.
     *
     * @param asyncWeatherService Service to fetch weather data asynchronously.
     */
    public WeatherReportController(AsyncWeatherService asyncWeatherService) {
        this.asyncWeatherService = asyncWeatherService;
    }

    /**
     * Endpoint to fetch weather data for origin and destination cities with flight number.
     *
     * @param flightNumber Flight number for the trip.
     * @param originCityName Name of the origin city.
     * @param originCountryCode ISO 3166 code of the origin country's region.
     * @param destCityName Name of the destination city.
     * @param destCountryCode ISO 3166 code of the destination country's region.
     * @return A ResponseEntity containing the JSON response with simplified weather data and flight details.
     */
    @GetMapping("/{flightNumber}/{originCityName}/{originCountryCode}/{destCityName}/{destCountryCode}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getWeatherReport(
            @PathVariable String flightNumber,
            @PathVariable String originCityName,
            @PathVariable String originCountryCode,
            @PathVariable String destCityName,
            @PathVariable String destCountryCode) {

        // Fetch weather data concurrently for both cities.
        CompletableFuture<Map<String, Object>> originWeather =
                asyncWeatherService.getSimplifiedWeatherAsync(originCityName, originCountryCode);
        CompletableFuture<Map<String, Object>> destinationWeather =
                asyncWeatherService.getSimplifiedWeatherAsync(destCityName, destCountryCode);

        // Combine both results into a single JSON response with flight details.
        return originWeather.thenCombine(destinationWeather, (origin, destination) -> {
            Map<String, Object> response = new HashMap<>();
            response.put("flightNumber", flightNumber);  // Add flight number

            // Weather data for origin.
            response.put("origin", origin);

            // Handle errors gracefully for destination weather data.
            if (destination.containsKey("error")) {
                response.put("destination", Map.of(
                        "message", destination.get("message"),
                        "error", destination.get("error")
                ));
            } else {
                response.put("destination", destination);
            }

            return ResponseEntity.ok(response);
        }).exceptionally(ex -> ResponseEntity.status(500)
                .body(Map.of("error", "An unexpected error occurred: " + ex.getMessage())));
    }
}
