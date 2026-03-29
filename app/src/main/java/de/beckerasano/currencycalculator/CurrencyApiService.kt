package de.beckerasano.currencycalculator

import de.beckerasano.currencycalculator.CurrencyResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class CurrencyApiService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getLatestRates(base: String): CurrencyResponse {
        return client.get("https://api.frankfurter.app/latest?from=$base").body()
    }

    suspend fun getSupportedCurrencies(): Map<String, String> {
        return client.get("https://api.frankfurter.app/currencies").body()
    }
}