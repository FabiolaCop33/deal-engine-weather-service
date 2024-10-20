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
 * 
 * This class uses Lombok annotations to reduce boilerplate code and 
 * JPA annotations to define the entity and its primary key.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity // Marks this class as a JPA entity to map it to a database table.
public class WeatherReport {

    /**
     * The unique identifier for each weather report (Primary Key).
     * Uses auto-generated value strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the city where the weather report is generated.
     */
    private String city;

    /**
     * The temperature recorded in the city (in Celsius).
     */
    private double temperature;

    /**
     * A brief description of the weather (e.g., "Clear sky", "Rainy").
     */
    private String description;

    /**
     * The humidity percentage recorded in the city.
     */
    private int humidity;
}
