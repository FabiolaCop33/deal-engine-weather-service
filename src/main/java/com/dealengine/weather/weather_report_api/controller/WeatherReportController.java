package com.dealengine.weather.weather_report_api.controller;

import com.dealengine.weather.weather_report_api.service.AsyncWeatherService;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to handle requests for weather reports.
 */
@RestController
@RequestMapping("/api/weather")
public class WeatherReportController {

	private final AsyncWeatherService asyncWeatherService;
    
	
	public WeatherReportController(AsyncWeatherService asyncWeatherService) {
        this.asyncWeatherService = asyncWeatherService;
    }

    /**
     * Endpoint to fetch weather data for origin and destination airports concurrently.
     *
     * @param originLat Latitude of the origin airport.
     * @param originLon Longitude of the origin airport.
     * @param destLat Latitude of the destination airport.
     * @param destLon Longitude of the destination airport.
     * @return JSON response with weather data for both airports.
     */
    @GetMapping("/{originLat}/{originLon}/{destLat}/{destLon}")
    public CompletableFuture<String> getWeatherReport(
            @PathVariable double originLat,
            @PathVariable double originLon,
            @PathVariable double destLat,
            @PathVariable double destLon) {

        // Fetch weather data concurrently for both airports.
        CompletableFuture<String> originWeather =
                asyncWeatherService.getWeatherAsync(originLat, originLon);
        CompletableFuture<String> destinationWeather =
                asyncWeatherService.getWeatherAsync(destLat, destLon);

        // Combine both results into a single JSON response.
        return originWeather.thenCombine(destinationWeather,
                (origin, destination) -> String.format(
                        "{\"originWeather\":%s,\"destinationWeather\":%s}",
                        origin, destination));
    }
}