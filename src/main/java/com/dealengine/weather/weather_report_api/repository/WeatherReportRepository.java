package com.dealengine.weather.weather_report_api.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.dealengine.weather.weather_report_api.model.WeatherReport;
import java.util.Optional;

/**
 * Repository interface to handle database operations for WeatherReport.
 */
public interface WeatherReportRepository extends JpaRepository<WeatherReport, Long> {

    /**
     * Finds a weather report by departure and destination cities.
     * 
     * @param departureCity The city of departure.
     * @param destinationCity The city of destination.
     * @return An Optional containing the WeatherReport if found, or empty otherwise.
     */
    Optional<WeatherReport> findByDepartureCityAndDestinationCity(String departureCity, String destinationCity);
}
