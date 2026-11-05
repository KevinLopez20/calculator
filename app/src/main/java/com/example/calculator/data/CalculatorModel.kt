package com.example.calculator.data

import java.text.DecimalFormat

/**
 * Lógica pura de la calculadora. Sin Android, sin Compose.
 * Nunca lanza excepciones: devuelve Double o CalcError.
 */
class CalculatorModel {

    enum class Operator { ADD, SUBTRACT, MULTIPLY, DIVIDE }

    sealed class CalcError {
        object DivisionByZero : CalcError()
        object InvalidInput : CalcError()
    }

    data class State(
        val currentInput: String = "0",
        val firstOperand: Double = 0.0,
        val pendingOperator: Operator? = null,
        val waitingSecondOperand: Boolean = false,
        val lastOperator: Operator? = null,
        val lastOperand: Double = 0.0,
        val error: CalcError? = null
    )

    private val formatter = DecimalFormat("#.##########")

    /** 8.0 → "8", 2.5 → "2.5" */
    fun format(value: Double): String = formatter.format(value)

    fun inputDigit(state: State, digit: String): State {
        val base = if (state.error != null) State() else state

        if (base.waitingSecondOperand) {
            return base.copy(currentInput = digit, waitingSecondOperand = false, error = null)
        }
        if (base.currentInput == "0" && digit != ".") {
            return base.copy(currentInput = digit)
        }
        val digitCount = base.currentInput
            .replace("-", "").replace(".", "").length
        if (digitCount >= 9) return base

        if (digit == "." && base.currentInput.contains(".")) return base

        return base.copy(currentInput = base.currentInput + digit)
    }

    fun setOperator(state: State, op: Operator): State {
        if (state.error != null) return state
        val inputValue = state.currentInput.toDoubleOrNull()
            ?: return state.copy(error = CalcError.InvalidInput)

        return if (state.pendingOperator != null && !state.waitingSecondOperand) {
            when (val result = calculate(state.firstOperand, inputValue, state.pendingOperator)) {
                is Double -> state.copy(
                    firstOperand = result,
                    currentInput = format(result),
                    pendingOperator = op,
                    waitingSecondOperand = true
                )
                is CalcError -> state.copy(error = result)
                else -> state.copy(error = CalcError.InvalidInput)
            }
        } else {
            state.copy(
                firstOperand = inputValue,
                pendingOperator = op,
                waitingSecondOperand = true
            )
        }
    }

    /** Devuelve el nuevo estado y el resultado (null si no hay nada que calcular) */
    fun equals(state: State): Pair<State, Double?> {
        if (state.error != null) return state to null

        return when {
            state.pendingOperator != null -> {
                val second = state.currentInput.toDoubleOrNull()
                    ?: return state.copy(error = CalcError.InvalidInput) to null
                when (val result = calculate(state.firstOperand, second, state.pendingOperator)) {
                    is Double -> state.copy(
                        currentInput = format(result),
                        pendingOperator = null,
                        waitingSecondOperand = true,
                        lastOperator = state.pendingOperator,
                        lastOperand = second
                    ) to result
                    is CalcError -> state.copy(error = result) to null
                    else -> state.copy(error = CalcError.InvalidInput) to null
                }
            }
            state.lastOperator != null -> {
                val current = state.currentInput.toDoubleOrNull()
                    ?: return state.copy(error = CalcError.InvalidInput) to null
                when (val result = calculate(current, state.lastOperand, state.lastOperator)) {
                    is Double -> state.copy(
                        currentInput = format(result),
                        waitingSecondOperand = true
                    ) to result
                    is CalcError -> state.copy(error = result) to null
                    else -> state.copy(error = CalcError.InvalidInput) to null
                }
            }
            else -> state to null
        }
    }

    fun percent(state: State): State {
        val value = state.currentInput.toDoubleOrNull()
            ?: return state.copy(error = CalcError.InvalidInput)
        val result = if (state.pendingOperator != null && !state.waitingSecondOperand) {
            state.firstOperand * value / 100.0
        } else value / 100.0
        return state.copy(currentInput = format(result))
    }

    fun toggleSign(state: State): State {
        if (state.currentInput == "0" || state.error != null) return state
        val newInput = if (state.currentInput.startsWith("-"))
            state.currentInput.removePrefix("-")
        else "-${state.currentInput}"
        return state.copy(currentInput = newInput)
    }

    fun clear(): State = State()

    private fun calculate(a: Double, b: Double, op: Operator): Any = when (op) {
        Operator.ADD -> a + b
        Operator.SUBTRACT -> a - b
        Operator.MULTIPLY -> a * b
        Operator.DIVIDE -> if (b == 0.0) CalcError.DivisionByZero else a / b
    }
}