package com.d29.auraplus.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.d29.auraplus.model.UserProfile

@Dao
interface UserProfileDao {
    @Insert
    suspend fun insertUser(user: UserProfile)

    @Query("SELECT * FROM user_profiles WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUser(email: String, password: String): UserProfile?

    @Query("SELECT COUNT(*) FROM user_profiles")
    suspend fun getUsersCount(): Int

}
