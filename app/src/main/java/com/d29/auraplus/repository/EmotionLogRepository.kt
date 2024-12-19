

package com.d29.auraplus.repository

import com.d29.auraplus.db.EmotionLogDao
import com.d29.auraplus.model.EmotionLog
import kotlinx.coroutines.flow.Flow

class EmotionLogRepository(private val dao: EmotionLogDao) {
    fun getAllEmotions(): Flow<List<EmotionLog>> = dao.getAllEmotions()

    suspend fun insertEmotion(emotion: EmotionLog) {
        dao.insertEmotion(emotion)
    }

    suspend fun updateEmotion(id: Int, name: String, notes: String) {
        dao.updateEmotion(id, name, notes)
    }

    suspend fun deleteEmotion(id: Int) {
        dao.deleteEmotion(id)
    }
}
