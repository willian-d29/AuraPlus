package com.d29.auraplus.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val email: String,  // Se usará como identificador único
    val name: String,
    val password: String
)

