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
    private final Map<String, String> airportMap; // Holds airport names for each IATA code.

    /**
     * Constructor for dependency injection and initializing the airport map.
     *
     * @param asyncWeatherService Service to fetch weather data asynchronously.
     */
    public WeatherReportController(AsyncWeatherService asyncWeatherService) {
        this.asyncWeatherService = asyncWeatherService;
        this.airportMap = initializeAirportMap(); // Load the airport data.
    }

    /**
     * Initializes a map of IATA codes to airport names using the dataset.
     * Replace with appropriate logic to load your dataset dynamically if needed.
     *
     * @return A map with IATA codes as keys and airport names as values.
     */
    private Map<String, String> initializeAirportMap() {
        Map<String, String> map = new HashMap<>();
        map.put("TLC", "Licenciado Adolfo Lopez Mateos International Airport");
        map.put("MTY", "General Mariano Escobedo International Airport");
        map.put("MEX", "Licenciado Benito Juarez International Airport");
        // Add more airport mappings from your dataset as needed.
        return map;
    }

    /**
     * Endpoint to fetch weather data and flight details concurrently.
     *
     * @param flightNumber     Flight number for the trip.
     * @param originCode       IATA code of the origin airport.
     * @param destinationCode  IATA code of the destination airport.
     * @return A ResponseEntity containing the JSON response with flight and weather data.
     */
    @GetMapping("/{flightNumber}/{originCode}/{destinationCode}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getFlightWeatherReport(
            @PathVariable String flightNumber,
            @PathVariable String originCode,
            @PathVariable String destinationCode) {

        // Fetch weather data for origin and destination concurrently.
        CompletableFuture<Map<String, Object>> originWeather =
                asyncWeatherService.getSimplifiedWeatherAsync(originCode, "MX");
        CompletableFuture<Map<String, Object>> destinationWeather =
                asyncWeatherService.getSimplifiedWeatherAsync(destinationCode, "MX");

        // Combine weather data with flight details.
        return originWeather.thenCombine(destinationWeather, (origin, destination) -> {
            Map<String, Object> response = new HashMap<>();
            response.put("flightNumber", flightNumber);

            // Flight details: Airport names based on IATA codes.
            response.put("originAirport", airportMap.getOrDefault(originCode, "Unknown Airport"));
            response.put("destinationAirport", airportMap.getOrDefault(destinationCode, "Unknown Airport"));

            // Weather data: Include error handling if destination weather is not available.
            response.put("origin", origin);
            response.put("destination", destination.containsKey("error")
                    ? Map.of("message", destination.get("message"), "error", destination.get("error"))
                    : destination);

            return ResponseEntity.ok(response);
        }).exceptionally(ex -> ResponseEntity.status(500)
                .body(Map.of("error", "An unexpected error occurred: " + ex.getMessage())));
    }

}
