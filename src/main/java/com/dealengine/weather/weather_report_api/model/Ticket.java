package com.dealengine.weather.weather_report_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a flight ticket entity with origin and destination airport details.
 * 
 * This entity maps to a database table using JPA annotations.
 * It stores all the required fields according to the challenge dataset.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity // Marks this class as a JPA entity for ORM.

public class Ticket {

    /**
     * Unique identifier for the ticket (Primary Key).
     * The ID is auto-generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The IATA code of the origin airport.
     * Example: "MEX" for Mexico City International Airport.
     */
    private String originIataCode;

    /**
     * The IATA code of the destination airport.
     * Example: "MTY" for Monterrey International Airport.
     */
    private String destinationIataCode;

    /**
     * The name of the origin airport.
     */
    private String originName;

    /**
     * The name of the destination airport.
     */
    private String destinationName;

    /**
     * Latitude of the origin airport.
     */
    private double originLatitude;

    /**
     * Longitude of the origin airport.
     */
    private double originLongitude;

    /**
     * Latitude of the destination airport.
     */
    private double destinationLatitude;

    /**
     * Longitude of the destination airport.
     */
    private double destinationLongitude;

    /**
     * The airline code operating the flight.
     * Example: "4O" for Interjet.
     */
    private String airline;

    /**
     * The flight number associated with the ticket.
     */
    private String flightNum;
}
