package de.beckerasano.currencycalculator

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyResponse(
    val amount: Double,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)

@Serializable
data class CurrenciesResponse(
    val currencies: Map<String, String> // Map of Code to Name (e.g., "USD" to "United States Dollar")
)