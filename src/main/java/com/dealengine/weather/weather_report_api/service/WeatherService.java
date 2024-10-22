package com.dealengine.weather.weather_report_api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;

/**
 * Service to interact with external weather APIs using geographic coordinates.
 * This service leverages RestTemplate to perform HTTP requests asynchronously.
 */
@Service
public class WeatherService {

    private final String apiKey = "c0e8d1d0c2a83784be52e988ff6550b8"; 
    private final String weatherApiUrl = "https://api.openweathermap.org/data/2.5/weather";

    private final RestTemplate restTemplate;

    /**
     * Constructor to initialize the RestTemplate.
     */
    public WeatherService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Fetches weather information for the given latitude and longitude.
     *
     * @param latitude  Latitude of the location.
     * @param longitude Longitude of the location.
     * @return A JSON string containing weather information.
     */
    public String getWeather(double latitude, double longitude) {
        String uri = UriComponentsBuilder.fromHttpUrl(weatherApiUrl)
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .toUriString();

        return restTemplate.getForObject(uri, String.class);
    }

    /**
     * Asynchronously fetches weather data using CompletableFuture.
     * 
     * @param latitude  Latitude of the location.
     * @param longitude Longitude of the location.
     * @return CompletableFuture with the weather report JSON as a String.
     */
    public CompletableFuture<String> getWeatherAsync(double latitude, double longitude) {
        return CompletableFuture.supplyAsync(() -> getWeather(latitude, longitude));
    }
}
