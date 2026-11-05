package com.example.calculator.data

data class HistoryEntry(
    val expression: String,
    val result: String
) {
    /** Formato pedido por el curso: "5 + 3 = 8" */
    fun toDisplayText(): String = "$expression = $result"
}