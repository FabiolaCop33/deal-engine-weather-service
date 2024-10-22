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
 * Uses concurrent processing to fetch weather data for origin and destination airports.
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
     * Endpoint to fetch weather data and flight details concurrently.
     *
     * @param flightNumber Flight number for the trip.
     * @param originCode IATA code of the origin airport.
     * @param destinationCode IATA code of the destination airport.
     * @return A ResponseEntity containing the JSON response with flight and weather data.
     */
    @GetMapping("/{flightNumber}/{originCode}/{destinationCode}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getFlightWeatherReport(
            @PathVariable String flightNumber,
            @PathVariable String originCode,
            @PathVariable String destinationCode) {

        // Fetch weather data for origin and destination concurrently.
        CompletableFuture<Map<String, Object>> originWeather =
                asyncWeatherService.getSimplifiedWeatherAsync(originCode);
        CompletableFuture<Map<String, Object>> destinationWeather =
                asyncWeatherService.getSimplifiedWeatherAsync(destinationCode);

        // Combine weather data with flight details.
        return originWeather.thenCombine(destinationWeather, (origin, destination) -> {
            Map<String, Object> response = new HashMap<>();
            response.put("flightNumber", flightNumber);

            // Flight details.
            response.put("originAirport", origin.getOrDefault("airport", "Unknown Airport"));
            response.put("destinationAirport", destination.getOrDefault("airport", "Unknown Airport"));

            // Weather data.
            response.put("origin", origin);
            response.put("destination", destination.containsKey("error")
                    ? Map.of("message", destination.get("message"), "error", destination.get("error"))
                    : destination);

            return ResponseEntity.ok(response);
        }).exceptionally(ex -> ResponseEntity.status(500)
                .body(Map.of("error", "An unexpected error occurred: " + ex.getMessage())));
    }
}
