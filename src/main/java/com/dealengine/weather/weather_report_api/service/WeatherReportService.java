package com.dealengine.weather.weather_report_api.service;

import com.dealengine.weather.weather_report_api.repository.WeatherReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutionException;

/**
 * Service layer responsible for generating weather reports by interacting with
 * WeatherService to fetch real-time weather data.
 */
@Service
public class WeatherReportService {

    private final WeatherReportRepository weatherReportRepository;
    private final WeatherService weatherService;

    /**
     * Constructor-based Dependency Injection for WeatherReportRepository and WeatherService.
     */
    @Autowired
    public WeatherReportService(WeatherReportRepository weatherReportRepository, 
                                WeatherService weatherService) {
        this.weatherReportRepository = weatherReportRepository;
        this.weatherService = weatherService;
    }

    /**
     * Generates a weather report using geographic coordinates for both origin and destination.
     *
     * @param originLatitude  Latitude of the origin airport.
     * @param originLongitude Longitude of the origin airport.
     * @param destLatitude    Latitude of the destination airport.
     * @param destLongitude   Longitude of the destination airport.
     * @return A JSON string with the weather reports for origin and destination.
     */
    public String generateReport(double originLatitude, double originLongitude, 
                                 double destLatitude, double destLongitude) {
        try {
            // Fetch weather reports concurrently
            String originWeather = weatherService.getWeatherAsync(originLatitude, originLongitude).get();
            String destinationWeather = weatherService.getWeatherAsync(destLatitude, destLongitude).get();

            return String.format("{\"origin\": %s, \"destination\": %s}", originWeather, destinationWeather);

        } catch (InterruptedException | ExecutionException e) {
            return "{\"error\":\"Failed to fetch weather reports\"}";
        }
    }
}
