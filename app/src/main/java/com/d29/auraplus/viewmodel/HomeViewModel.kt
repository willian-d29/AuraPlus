package com.d29.auraplus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.d29.auraplus.model.EmotionLog
import com.d29.auraplus.repository.EmotionLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar


class HomeViewModel(private val repository: EmotionLogRepository) : ViewModel() {
    val allEmotions: Flow<List<EmotionLog>> = repository.getAllEmotions()

    fun addEmotion(name: String, notes: String) {
        val emotion = EmotionLog(
            name = name,
            notes = notes,
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch { repository.insertEmotion(emotion) }
    }

    fun updateEmotion(id: Int, name: String, notes: String) {
        viewModelScope.launch {
            repository.updateEmotion(id, name, notes)
        }
    }

    fun editEmotion(id: Int, newName: String, newNotes: String) {
        updateEmotion(id, newName, newNotes) // Alias para updateEmotion
    }

    fun deleteEmotion(id: Int) {
        viewModelScope.launch {
            repository.deleteEmotion(id)
        }
    }

    // Obtener estadísticas mensuales
    fun getMonthlyStatistics(month: Int, year: Int): Flow<Map<String, Int>> {
        return repository.getAllEmotions().map { emotions ->
            val monthlyEmotions = emotions.filter {
                val calendar = Calendar.getInstance().apply { timeInMillis = it.timestamp }
                calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.YEAR) == year
            }
            // Contar emociones por tipo
            monthlyEmotions.groupingBy { it.name }.eachCount()
        }
    }

    // Factory para inicializar HomeViewModel
    companion object {
        fun Factory(repository: EmotionLogRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                        return HomeViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}
