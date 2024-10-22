package com.dealengine.weather.weather_report_api.controller;

import com.dealengine.weather.weather_report_api.service.AsyncWeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;
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
     * Endpoint to fetch weather data for origin and destination airports concurrently.
     *
     * @param originCode IATA code of the origin airport.
     * @param destinationCode IATA code of the destination airport.
     * @return A ResponseEntity containing the JSON response with simplified weather data.
     */
    @GetMapping("/{originCode}/{destinationCode}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getWeatherReport(
            @PathVariable String originCode,
            @PathVariable String destinationCode) {

        CompletableFuture<Map<String, Object>> originWeather =
                asyncWeatherService.getSimplifiedWeatherAsync(originCode);
        CompletableFuture<Map<String, Object>> destinationWeather =
                asyncWeatherService.getSimplifiedWeatherAsync(destinationCode);

        return originWeather.thenCombine(destinationWeather, (origin, destination) -> {
            Map<String, Object> response = Map.of(
                    "origin", origin,
                    "destination", destination
            );
            return ResponseEntity.ok(response);
        }).exceptionally(ex -> ResponseEntity.status(500).body(
                Map.of("message", "Failed to fetch weather data", "error", ex.getMessage())
        ));
    }
}
