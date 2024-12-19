package com.d29.auraplus.db

import androidx.room.*
import com.d29.auraplus.model.EmotionLog
import kotlinx.coroutines.flow.Flow

@Dao
interface EmotionLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmotion(emotion: EmotionLog)

    @Query("SELECT * FROM emotion_logs ORDER BY timestamp DESC")
    fun getAllEmotions(): Flow<List<EmotionLog>>

    @Query("UPDATE emotion_logs SET name = :name, notes = :notes WHERE id = :id")
    suspend fun updateEmotion(id: Int, name: String, notes: String)

    @Query("DELETE FROM emotion_logs WHERE id = :id")
    suspend fun deleteEmotion(id: Int)
}
