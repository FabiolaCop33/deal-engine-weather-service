package com.dealengine.weather.weather_report_api.repository;


import com.dealengine.weather.weather_report_api.model.WeatherReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing WeatherReport entities from the database.
 * 
 * This interface extends JpaRepository, providing built-in CRUD operations
 * and the ability to define custom query methods based on naming conventions.
 */
@Repository // Marks this interface as a Spring-managed bean for dependency injection.
public interface WeatherReportRepository extends JpaRepository<WeatherReport, Long> {

    /**
     * Retrieves a list of weather reports for a specific city.
     * 
     * @param city The name of the city to filter reports by.
     * @return A list of WeatherReport entities matching the city name.
     */
    List<WeatherReport> findByCity(String city);
}
