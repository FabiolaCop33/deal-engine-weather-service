package com.dealengine.weather.weather_report_api.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WeatherCacheService handles caching of weather data to avoid redundant API calls.
 * It stores weather data temporarily using in-memory cache and removes stale entries.
 *
 * This class helps meet the concurrency and performance requirements
 * by reducing the load on the weather API.
 */
@Service
public class WeatherCacheService {

    /**
     * A cache entry that holds the weather data and the timestamp it was stored.
     */
    private static class CacheEntry {
        String weatherData;
        Instant timestamp;

        CacheEntry(String weatherData) {
            this.weatherData = weatherData;
            this.timestamp = Instant.now();
        }
    }

    // In-memory cache using a thread-safe ConcurrentHashMap.
    private final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    // Cache expiry time in minutes.
    private static final long CACHE_EXPIRATION_MINUTES = 10;

    /**
     * Retrieves weather data from the cache, or returns null if the data is expired or not found.
     *
     * @param key The key to identify the weather data (e.g., "lat:lon").
     * @return The cached weather data, or null if expired or not found.
     */
    public String getWeatherFromCache(String key) {
        CacheEntry entry = cache.get(key);

        if (entry == null) {
            return null; // No entry found in the cache.
        }

        // Check if the cached entry has expired.
        if (Duration.between(entry.timestamp, Instant.now()).toMinutes() >= CACHE_EXPIRATION_MINUTES) {
            cache.remove(key); // Remove expired entry.
            return null;
        }

        return entry.weatherData;
    }

    /**
     * Stores weather data in the cache with the given key.
     *
     * @param key The key to store the weather data (e.g., "lat:lon").
     * @param weatherData The weather data to cache.
     */
    public void storeWeatherInCache(String key, String weatherData) {
        cache.put(key, new CacheEntry(weatherData));
    }
}
