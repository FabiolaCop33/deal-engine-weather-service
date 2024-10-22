package com.dealengine.weather.weather_report_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main class to bootstrap the Spring Boot application.
 * 
 * @EnableAsync is used to enable asynchronous processing.
 */
@SpringBootApplication
@EnableAsync
public class WeatherReportApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherReportApiApplication.class, args);
    }
}
