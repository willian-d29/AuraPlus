package com.d29.auraplus.repository

import com.d29.auraplus.db.UserProfileDao
import com.d29.auraplus.model.UserProfile

class UserRepository(private val userDao: UserProfileDao) {
     suspend fun insertUser(user: UserProfile) = userDao.insertUser(user)
     suspend fun getUser(email: String, password: String): UserProfile? = userDao.getUser(email, password)
     suspend fun hasUsers(): Boolean = userDao.getUsersCount() > 0
}
