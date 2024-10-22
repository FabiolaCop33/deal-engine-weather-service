package com.dealengine.weather.weather_report_api.controller;

import com.dealengine.weather.weather_report_api.service.AsyncWeatherService;
import java.util.concurrent.CompletableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

/**
 * Controller to handle requests for weather reports.
 * 
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
     * Endpoint to fetch weather data for origin and destination airports based on IATA codes.
     *
     * @param originIata The IATA code of the origin airport.
     * @param destinationIata The IATA code of the destination airport.
     * @return A ResponseEntity containing the JSON response with weather data for both airports.
     */
    @GetMapping("/{originIata}/{destinationIata}")
    public CompletableFuture<ResponseEntity<String>> getWeatherReport(
            @PathVariable String originIata,
            @PathVariable String destinationIata) {

        // Fetch weather data asynchronously for both airports based on their IATA codes.
        CompletableFuture<String> originWeather = 
                asyncWeatherService.getWeatherByIataCodeAsync(originIata);
        CompletableFuture<String> destinationWeather = 
                asyncWeatherService.getWeatherByIataCodeAsync(destinationIata);

        // Combine both weather data results into a single JSON response.
        return originWeather.thenCombine(destinationWeather,
                (origin, destination) -> String.format(
                        "{\"originWeather\":%s,\"destinationWeather\":%s}", origin, destination))
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500)
                        .body(String.format("{\"error\":\"Failed to fetch weather data: %s\"}", ex.getMessage())));
    }
}
