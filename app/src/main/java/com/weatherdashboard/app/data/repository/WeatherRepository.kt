package com.weatherdashboard.app.data.repository

import android.content.Context
import com.weatherdashboard.app.BuildConfig
import com.weatherdashboard.app.data.api.WeatherApiService
import com.weatherdashboard.app.data.dao.CachedWeatherDao
import com.weatherdashboard.app.data.dao.FavoriteCityDao
import com.weatherdashboard.app.data.dao.SearchHistoryDao
import com.weatherdashboard.app.data.model.CachedWeather
import com.weatherdashboard.app.data.model.FavoriteCity
import com.weatherdashboard.app.data.model.ForecastResponse
import com.weatherdashboard.app.data.model.GeocodingResponse
import com.weatherdashboard.app.data.model.SearchHistory
import com.weatherdashboard.app.data.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class WeatherRepository(
    private val apiService: WeatherApiService,
    private val favoriteCityDao: FavoriteCityDao,
    private val cachedWeatherDao: CachedWeatherDao,
    private val searchHistoryDao: SearchHistoryDao
) {

    private val apiKey = BuildConfig.OPENWEATHER_API_KEY
    private val cacheTimeout = TimeUnit.HOURS.toMillis(1) // 1 hour cache

    // Current Weather
    suspend fun getCurrentWeatherByCity(cityName: String): Result<WeatherResponse> {
        return try {
            val response = apiService.getCurrentWeatherByCity(cityName, "metric", apiKey)
            cacheWeather(response)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentWeatherByCoordinates(
        latitude: Double,
        longitude: Double
    ): Result<WeatherResponse> {
        return try {
            val response = apiService.getCurrentWeatherByCoordinates(
                latitude, longitude, "metric", apiKey
            )
            cacheWeather(response)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWeatherFromCache(cityId: Int): CachedWeather? {
        return cachedWeatherDao.getCachedWeather(cityId)
    }

    // Forecast
    suspend fun getForecastByCity(cityName: String): Result<ForecastResponse> {
        return try {
            val response = apiService.getForecast(cityName, "metric", apiKey)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getForecastByCoordinates(
        latitude: Double,
        longitude: Double
    ): Result<ForecastResponse> {
        return try {
            val response = apiService.getForecastByCoordinates(
                latitude, longitude, "metric", apiKey
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Geocoding
    suspend fun searchCities(cityName: String): Result<List<GeocodingResponse>> {
        return try {
            val response = apiService.getCoordinates(cityName, 5, apiKey)
            // Save search history
            response.forEach { location ->
                searchHistoryDao.insert(
                    SearchHistory(
                        cityName = location.name,
                        country = location.country
                    )
                )
            }
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLocationName(latitude: Double, longitude: Double): Result<GeocodingResponse> {
        return try {
            val response = apiService.getLocationName(latitude, longitude, 1, apiKey)
            Result.success(response.firstOrNull() ?: throw Exception("Location not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Favorites
    suspend fun addFavorite(city: FavoriteCity) {
        favoriteCityDao.insert(city)
    }

    suspend fun removeFavorite(city: FavoriteCity) {
        favoriteCityDao.delete(city)
    }

    fun getFavoriteCities(): Flow<List<FavoriteCity>> {
        return favoriteCityDao.getAllFavoriteCities()
    }

    suspend fun isFavorite(cityId: Int): Boolean {
        return favoriteCityDao.isFavorite(cityId) > 0
    }

    // Search History
    fun getSearchHistory(): Flow<List<SearchHistory>> {
        return searchHistoryDao.getSearchHistory()
    }

    suspend fun clearSearchHistory() {
        searchHistoryDao.clearAll()
    }

    // Cache Management
    private suspend fun cacheWeather(weather: WeatherResponse) {
        val cached = CachedWeather(
            cityId = weather.id,
            cityName = weather.name,
            latitude = weather.coord.lat,
            longitude = weather.coord.lon,
            temperature = weather.main.temp,
            feelsLike = weather.main.feels_like,
            tempMin = weather.main.temp_min,
            tempMax = weather.main.temp_max,
            humidity = weather.main.humidity,
            pressure = weather.main.pressure,
            description = weather.weather.firstOrNull()?.description ?: "",
            icon = weather.weather.firstOrNull()?.icon ?: "",
            windSpeed = weather.wind.speed,
            windDeg = weather.wind.deg,
            cloudiness = weather.clouds.all,
            sunrise = weather.sys.sunrise,
            sunset = weather.sys.sunset
        )
        cachedWeatherDao.insert(cached)
    }

    suspend fun clearOldCache() {
        val cutoffTime = System.currentTimeMillis() - cacheTimeout
        cachedWeatherDao.deleteOldEntries(cutoffTime)
    }
}
