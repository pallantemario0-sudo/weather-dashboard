package com.weatherdashboard.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.weatherdashboard.app.WeatherApplication
import com.weatherdashboard.app.databinding.ActivityDetailBinding
import com.weatherdashboard.app.ui.viewmodel.ForecastViewModel
import com.weatherdashboard.app.ui.viewmodel.ForecastUiState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: ForecastViewModel
    private var cityName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cityName = intent.getStringExtra("city_name") ?: "London"

        val app = application as WeatherApplication
        viewModel = ViewModelProvider(
            this,
            ForecastViewModelFactory(app.weatherRepository)
        )[ForecastViewModel::class.java]

        setupUI()
        observeViewModel()
        viewModel.getForecastByCity(cityName)
    }

    private fun setupUI() {
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.titleView.text = "Previsioni - $cityName"
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.forecastState.collect { state ->
                when (state) {
                    is ForecastUiState.Loading -> {
                        binding.progressBar.visibility = android.view.View.VISIBLE
                    }
                    is ForecastUiState.Success -> {
                        binding.progressBar.visibility = android.view.View.GONE
                        displayForecast(state.forecast)
                    }
                    is ForecastUiState.Error -> {
                        binding.progressBar.visibility = android.view.View.GONE
                    }
                    else -> {}
                }
            }
        }
    }

    private fun displayForecast(forecast: com.weatherdashboard.app.data.model.ForecastResponse) {
        val dateFormat = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
        val container = binding.forecastContainer

        forecast.list.forEach { item ->
            val date = dateFormat.format(Date(item.dt * 1000))
            val temp = String.format("%.1f°C", item.main.temp)
            val desc = item.weather.firstOrNull()?.description?.uppercase() ?: ""

            val forecastView = android.widget.LinearLayout(this).apply {
                layoutParams = android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                )
                orientation = android.widget.LinearLayout.VERTICAL
                setPadding(16, 8, 16, 8)
            }

            val dateView = android.widget.TextView(this).apply {
                text = date
                textSize = 14f
                setTextColor(android.graphics.Color.BLACK)
            }

            val tempView = android.widget.TextView(this).apply {
                text = temp
                textSize = 16f
                setTextColor(android.graphics.Color.BLUE)
            }

            val descView = android.widget.TextView(this).apply {
                text = desc
                textSize = 12f
                setTextColor(android.graphics.Color.GRAY)
            }

            forecastView.addView(dateView)
            forecastView.addView(tempView)
            forecastView.addView(descView)

            container.addView(forecastView)
        }
    }
}

class ForecastViewModelFactory(private val repository: com.weatherdashboard.app.data.repository.WeatherRepository) :
    androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ForecastViewModel(repository) as T
    }
}
