package com.example.calculator.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Fuente única de verdad del historial.
 * StateFlow = estado observable para Compose.
 */
class HistoryRepository {

    private val _entries = MutableStateFlow<List<HistoryEntry>>(emptyList())
    val entries: StateFlow<List<HistoryEntry>> = _entries.asStateFlow()

    fun add(entry: HistoryEntry) {
        _entries.value = _entries.value + entry
    }

    fun clear() {
        _entries.value = emptyList()
    }
}
