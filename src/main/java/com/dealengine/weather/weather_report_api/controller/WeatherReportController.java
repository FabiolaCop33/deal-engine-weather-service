package com.dealengine.weather.weather_report_api.controller;

import com.dealengine.weather.weather_report_api.service.WeatherReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to handle HTTP requests for weather reports.
 */
@RestController
public class WeatherReportController {

    private final WeatherReportService weatherReportService;

    /**
     * Constructor-based Dependency Injection for the WeatherReportService.
     * 
     * @param weatherReportService The service to handle weather report operations.
     */
    @Autowired
    public WeatherReportController(WeatherReportService weatherReportService) {
        this.weatherReportService = weatherReportService;
    }

    /**
     * Endpoint to generate a weather report based on departure and destination cities.
     * 
     * @param departureCity The city of departure.
     * @param destinationCity The city of destination.
     * @return JSON representation of the weather report or an error message.
     */
    @GetMapping("/weather-report")
    public String getWeatherReport(@RequestParam String departureCity, @RequestParam String destinationCity) {
        try {
            return weatherReportService.generateReport(departureCity, destinationCity);
        } catch (Exception e) {
            return "{\"error\":\"An error occurred while generating the report\"}";
        }
    }
}