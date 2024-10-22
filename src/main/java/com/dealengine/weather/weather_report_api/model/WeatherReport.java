package com.dealengine.weather.weather_report_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a weather report entity stored in the database.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WeatherReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The IATA code of the origin airport.
     */
    private String originIataCode;

    /**
     * The name of the origin airport.
     */
    private String originName;

    /**
     * The latitude of the origin airport.
     */
    private double originLatitude;

    /**
     * The longitude of the origin airport.
     */
    private double originLongitude;

    /**
     * The IATA code of the destination airport.
     */
    private String destinationIataCode;

    /**
     * The name of the destination airport.
     */
    private String destinationName;

    /**
     * The latitude of the destination airport.
     */
    private double destinationLatitude;

    /**
     * The longitude of the destination airport.
     */
    private double destinationLongitude;
}
