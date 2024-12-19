package com.d29.auraplus.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emotion_logs")
data class EmotionLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val notes: String,
    val timestamp: Long
)
