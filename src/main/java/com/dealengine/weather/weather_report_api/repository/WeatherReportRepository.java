package com.dealengine.weather.weather_report_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dealengine.weather.weather_report_api.model.WeatherReport;
import java.util.Optional;

/**
 * Repository interface to handle database operations for WeatherReport.
 */
public interface WeatherReportRepository extends JpaRepository<WeatherReport, Long> {

    /**
     * Finds a weather report by origin and destination IATA codes.
     * 
     * @param originIataCode The IATA code of the origin airport.
     * @param destinationIataCode The IATA code of the destination airport.
     * @return An Optional containing the WeatherReport if found, or empty otherwise.
     */
    Optional<WeatherReport> findByOriginIataCodeAndDestinationIataCode(String originIataCode, String destinationIataCode);
}
