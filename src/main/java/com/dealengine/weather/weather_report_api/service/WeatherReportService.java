package com.dealengine.weather.weather_report_api.service;

import com.dealengine.weather.weather_report_api.model.WeatherReport;
import com.dealengine.weather.weather_report_api.repository.WeatherReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

/**
 * Service layer responsible for handling the business logic related to Weather Reports.
 * This class interacts with the WeatherReportRepository to fetch and save data.
 * 
 * Pattern: Service Layer
 * - Separates business logic from controllers, promoting modularity and reusability.
 */
@Service
public class WeatherReportService {

    private final WeatherReportRepository weatherReportRepository;
    private final ObjectMapper objectMapper;

    /**
     * Constructor-based Dependency Injection.
     * Ensures that the WeatherReportRepository and ObjectMapper are properly injected.
     * 
     * @param weatherReportRepository Repository to access weather data.
     * @param objectMapper JSON utility for serialization.
     */
    @Autowired
    public WeatherReportService(WeatherReportRepository weatherReportRepository, ObjectMapper objectMapper) {
        this.weatherReportRepository = weatherReportRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Generates a JSON weather report for the given departure and destination cities.
     * 
     * @param departureCity The city of departure.
     * @param destinationCity The city of destination.
     * @return A JSON string representation of the WeatherReport.
     * @throws JsonProcessingException If there's an error during JSON processing.
     */
    public String generateReport(String departureCity, String destinationCity) throws JsonProcessingException {
        // Fetch the weather report from the database based on the cities provided
        Optional<WeatherReport> report = weatherReportRepository
            .findByDepartureCityAndDestinationCity(departureCity, destinationCity);

        if (report.isPresent()) {
            // Convert the found report to JSON
            return objectMapper.writeValueAsString(report.get());
        } else {
            // If no report is found, return a JSON error message
            return "{\"error\":\"No weather report found for the given cities\"}";
        }
    }
}
