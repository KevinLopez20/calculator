package com.example.calculator


import com.example.calculator.data.CalculatorModel
import com.example.calculator.data.CalculatorModel.Operator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculatorModelTest {

    private val model = CalculatorModel()
    private fun initial() = CalculatorModel.State()

    @Test
    fun `suma simple devuelve 8`() {
        var s = model.inputDigit(initial(), "5")
        s = model.setOperator(s, Operator.ADD)
        s = model.inputDigit(s, "3")
        val (final, result) = model.equals(s)
        assertEquals(8.0, result!!, 0.001)
        assertEquals("8", final.currentInput)
    }

    @Test
    fun `division por cero devuelve error semantico sin excepcion`() {
        var s = model.inputDigit(initial(), "5")
        s = model.setOperator(s, Operator.DIVIDE)
        s = model.inputDigit(s, "0")
        val (final, _) = model.equals(s)
        assertTrue(final.error is CalculatorModel.CalcError.DivisionByZero)
    }

    @Test
    fun `encadenamiento 5 mas 3 por 2 da 16`() {
        var s = model.inputDigit(initial(), "5")
        s = model.setOperator(s, Operator.ADD)
        s = model.inputDigit(s, "3")
        s = model.setOperator(s, Operator.MULTIPLY)
        s = model.inputDigit(s, "2")
        val (_, result) = model.equals(s)
        assertEquals(16.0, result!!, 0.001)
    }

    @Test
    fun `repetir equals repite la ultima operacion`() {
        var s = model.inputDigit(initial(), "5")
        s = model.setOperator(s, Operator.ADD)
        s = model.inputDigit(s, "3")
        var pair = model.equals(s)
        assertEquals(8.0, pair.second!!, 0.001)
        pair = model.equals(pair.first)
        assertEquals(11.0, pair.second!!, 0.001)
    }

    @Test
    fun `doble punto decimal es ignorado`() {
        var s = model.inputDigit(initial(), "1")
        s = model.inputDigit(s, ".")
        s = model.inputDigit(s, ".")
        s = model.inputDigit(s, "5")
        assertEquals("1.5", s.currentInput)
    }

    @Test
    fun `porcentaje con operacion pendiente usa primer operando`() {
        var s = model.inputDigit(initial(), "2")
        s = model.inputDigit(s, "0")
        s = model.inputDigit(s, "0")
        s = model.setOperator(s, Operator.ADD)
        s = model.inputDigit(s, "1")
        s = model.inputDigit(s, "0")
        s = model.percent(s)
        assertEquals("20", s.currentInput)  // 200 + 10% → segundo operando = 20
    }
}