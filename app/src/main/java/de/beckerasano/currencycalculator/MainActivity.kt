package de.beckerasano.currencycalculator

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import de.beckerasano.currencycalculator.databinding.ActivityMainBinding
import de.beckerasano.currencycalculator.CurrencyViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: CurrencyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            viewModel.lastAmount.collect { binding.etAmount.setText(it) }
        }
        // Observe currencies to fill Spinners
        lifecycleScope.launch {
            viewModel.currencies.collect { list ->
                val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, list)
                binding.spinnerFrom.adapter = adapter
                binding.spinnerTo.adapter = adapter
                // Restore Spinner selections
                val fromPos = list.indexOf(viewModel.lastFrom.value)
                if (fromPos >= 0) binding.spinnerFrom.setSelection(fromPos)

                val toPos = list.indexOf(viewModel.lastTo.value)
                if (toPos >= 0) binding.spinnerTo.setSelection(toPos)
            }
        }

        // Observe result
        lifecycleScope.launch {
            viewModel.conversionResult.collect { result ->
                binding.tvResult.text = result
            }
        }

        binding.btnConvert.setOnClickListener {
            val amount = binding.etAmount.text.toString().toDoubleOrNull() ?: 0.0
            val from = binding.spinnerFrom.selectedItem?.toString() ?: "EUR"
            val to = binding.spinnerTo.selectedItem?.toString() ?: "JPY"
            viewModel.convert(amount, from, to)
        }
        binding.btnSwap.setOnClickListener {
            val fromCurrency = binding.spinnerFrom.selectedItem?.toString() ?: "EUR"
            val toCurrency = binding.spinnerTo.selectedItem?.toString() ?: "JPY"

            // 1. Tell ViewModel to swap
            viewModel.swapCurrencies(fromCurrency, toCurrency)

            // 2. Update the Spinner selections in UI
            val adapter = binding.spinnerFrom.adapter as? ArrayAdapter<String>
            if (adapter != null) {
                val newFromPos = adapter.getPosition(toCurrency)
                val newToPos = adapter.getPosition(fromCurrency)

                if (newFromPos >= 0) binding.spinnerFrom.setSelection(newFromPos)
                if (newToPos >= 0) binding.spinnerTo.setSelection(newToPos)
            }
        }
    }
}