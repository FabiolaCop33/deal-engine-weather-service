package com.dealengine.weather.weather_report_api.controller;

import com.dealengine.weather.weather_report_api.service.WeatherReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlador REST que expone endpoints para la API de reportes de clima.
 * Este controlador utiliza el patrón de diseño "Controlador" para gestionar
 * la lógica de las solicitudes HTTP y delegar el procesamiento al servicio correspondiente.
 */
@RestController
public class WeatherReportController {

    // Inyección de dependencias para acceder al servicio de generación de reportes
    private final WeatherReportService weatherReportService;

    /**
     * Constructor del controlador que inyecta la instancia del servicio.
     * @param weatherReportService Servicio que genera los reportes de clima.
     */
    public WeatherReportController(WeatherReportService weatherReportService) {
        this.weatherReportService = weatherReportService;
    }

    /**
     * Endpoint que genera un reporte del clima entre dos ciudades.
     * @param departureCity Nombre de la ciudad de salida.
     * @param destinationCity Nombre de la ciudad de destino.
     * @return Un mapa (JSON) con el reporte del clima para ambas ciudades.
     *
     * Ejemplo de acceso:
     * GET /api/weather/report?departureCity=Paris&destinationCity=Berlin
     */
    @GetMapping("/api/weather/report")
    public Map<String, Object> getWeatherReport(
            @RequestParam String departureCity,
            @RequestParam String destinationCity) {

        // Llama al servicio para generar el reporte y retorna la respuesta como JSON
        return weatherReportService.generateReport(departureCity, destinationCity);
    }
}
