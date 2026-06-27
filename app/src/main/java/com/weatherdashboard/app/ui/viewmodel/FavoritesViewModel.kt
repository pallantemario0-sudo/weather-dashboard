package com.weatherdashboard.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherdashboard.app.data.model.FavoriteCity
import com.weatherdashboard.app.data.repository.WeatherRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: WeatherRepository) : ViewModel() {

    val favoriteCities: StateFlow<List<FavoriteCity>> = repository.getFavoriteCities()
        .let { flow ->
            // Convert Flow to StateFlow
            val stateFlow = MutableStateFlow<List<FavoriteCity>>(emptyList())
            viewModelScope.launch {
                flow.collect { cities ->
                    stateFlow.value = cities
                }
            }
            stateFlow
        }

    fun removeFavorite(city: FavoriteCity) {
        viewModelScope.launch {
            repository.removeFavorite(city)
        }
    }
}

// Helper extension to properly convert Flow to StateFlow
private fun <T> StateFlow(initialValue: T): MutableStateFlow<T> {
    return MutableStateFlow(initialValue)
}
