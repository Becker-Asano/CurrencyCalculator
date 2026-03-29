package de.beckerasano.currencycalculator

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.beckerasano.currencycalculator.CurrencyApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Changed from ViewModel to AndroidViewModel to access SharedPreferences
class CurrencyViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = CurrencyApiService()
    private val prefs = application.getSharedPreferences("currency_prefs", Context.MODE_PRIVATE)

    private val _currencies = MutableStateFlow<List<String>>(emptyList())
    val currencies: StateFlow<List<String>> = _currencies

    private val _conversionResult = MutableStateFlow<String>("")
    val conversionResult: StateFlow<String> = _conversionResult

    // State flows for the saved inputs
    private val _lastAmount = MutableStateFlow(prefs.getString("last_amount", "1.0") ?: "1.0")
    val lastAmount: StateFlow<String> = _lastAmount

    private val _lastFrom = MutableStateFlow(prefs.getString("last_from", "EUR") ?: "EUR")
    val lastFrom: StateFlow<String> = _lastFrom

    private val _lastTo = MutableStateFlow(prefs.getString("last_to", "USD") ?: "USD")
    val lastTo: StateFlow<String> = _lastTo

    init {
        loadCurrencies()
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            try {
                val list = apiService.getSupportedCurrencies().keys.toList().sorted()
                _currencies.value = list
            } catch (e: Exception) {
                _conversionResult.value = "Error loading currencies"
            }
        }
    }

    fun convert(amount: Double, from: String, to: String) {
        // Save settings to SharedPreferences
        prefs.edit().apply {
            putString("last_amount", amount.toString())
            putString("last_from", from)
            putString("last_to", to)
            apply()
        }

        if (amount <= 0.0) return
        viewModelScope.launch {
            try {
                val response = apiService.getLatestRates(from)
                val rate = response.rates[to] ?: 1.0
                val result = amount * rate
                _conversionResult.value = String.format("%.2f %s", result, to)
            } catch (e: Exception) {
                _conversionResult.value = "Conversion failed"
            }
        }
    }
    fun swapCurrencies(currentFrom: String, currentTo: String) {
        // Update the state flows (this will trigger observers in the UI)
        _lastFrom.value = currentTo
        _lastTo.value = currentFrom

        // Persist the swap to SharedPreferences immediately
        prefs.edit().apply {
            putString("last_from", currentTo)
            putString("last_to", currentFrom)
            apply()
        }
    }
}