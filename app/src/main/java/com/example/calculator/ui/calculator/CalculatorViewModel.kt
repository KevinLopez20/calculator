package com.example.calculator.ui.calculator

import androidx.lifecycle.ViewModel
import com.example.calculator.data.CalculatorModel
import com.example.calculator.data.CalculatorModel.Operator
import com.example.calculator.data.CalculatorModel.State
import com.example.calculator.data.HistoryEntry
import com.example.calculator.data.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * EXPONE estado observable (StateFlow) y recibe INTENCIONES de la UI.
 * Jamás toca stringResource/colorResource: solo estados semánticos.
 *
 * Sé instancia a nivel Activity para que calculadora e historial
 * compartan la MISMA instancia (mismo historial).
 */
class CalculatorViewModel : ViewModel() {

    private val model = CalculatorModel()
    private val historyRepository = HistoryRepository()

    private val _uiState = MutableStateFlow(State())
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    val history = historyRepository.entries

    // ===== INTENCIONES =====

    fun onDigitPressed(digit: String) {
        _uiState.value = model.inputDigit(_uiState.value, digit)
    }

    fun onOperatorSelected(op: Operator) {
        _uiState.value = model.setOperator(_uiState.value, op)
    }

    fun onEqualsPressed() {
        val state = _uiState.value
        // Capturar ANTES de mutar: solo operaciones nuevas, no "=" repetido
        val isNewOperation = state.pendingOperator != null
        val secondOperand = state.currentInput.toDoubleOrNull()

        val (newState, result) = model.equals(state)
        _uiState.value = newState

        if (result != null && newState.error == null && isNewOperation && secondOperand != null) {
            val symbol = when (state.pendingOperator) {
                Operator.ADD -> "+"
                Operator.SUBTRACT -> "−"
                Operator.MULTIPLY -> "×"
                Operator.DIVIDE -> "÷"
                null -> return
            }
            historyRepository.add(
                HistoryEntry(
                    expression = "${model.format(state.firstOperand)} $symbol ${model.format(secondOperand)}",
                    result = newState.currentInput
                )
            )
        }
    }

    fun onPercentPressed() {
        _uiState.value = model.percent(_uiState.value)
    }

    fun onSignToggled() {
        _uiState.value = model.toggleSign(_uiState.value)
    }

    fun onClearPressed() {
        _uiState.value = model.clear()
    }

    fun onErrorShown() {
        _uiState.value = _uiState.value.copy(error = null, currentInput = "0")
    }

    fun clearHistory() {
        historyRepository.clear()
    }
}