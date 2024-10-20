package com.dealengine.weather.weather_report_api.service;

import com.dealengine.weather.weather_report_api.model.WeatherReport;
import com.dealengine.weather.weather_report_api.repository.WeatherReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer responsible for handling the business logic related to Weather Reports.
 * This class interacts with the WeatherReportRepository to fetch and save data.
 * 
 * Pattern: Service Layer
 * - This pattern helps separate business logic from controllers,
 *   promoting modularity and reusability.
 */
@Service
public class WeatherReportService {

    private final WeatherReportRepository weatherReportRepository;

    /**
     * Constructor-based Dependency Injection.
     * Ensures that the WeatherReportRepository is properly injected when this service is instantiated.
     * 
     * @param weatherReportRepository Repository to access weather data.
     */
    @Autowired
    public WeatherReportService(WeatherReportRepository weatherReportRepository) {
        this.weatherReportRepository = weatherReportRepository;
    }

    /**
     * Fetches all weather reports from the database.
     * 
     * @return List of WeatherReport objects.
     */
    public List<WeatherReport> getAllReports() {
        return weatherReportRepository.findAll();
    }

    /**
     * Fetches a single weather report by its ID.
     * 
     * @param id The ID of the WeatherReport to retrieve.
     * @return An Optional containing the WeatherReport if found, or empty if not found.
     */
    public Optional<WeatherReport> getReportById(Long id) {
        return weatherReportRepository.findById(id);
    }

    /**
     * Saves a new weather report or updates an existing one in the database.
     * 
     * @param weatherReport The WeatherReport object to save or update.
     * @return The saved WeatherReport object.
     */
    public WeatherReport saveReport(WeatherReport weatherReport) {
        return weatherReportRepository.save(weatherReport);
    }

    /**
     * Deletes a weather report from the database by its ID.
     * 
     * @param id The ID of the WeatherReport to delete.
     */
    public void deleteReport(Long id) {
        weatherReportRepository.deleteById(id);
    }
}
