package com.nasiat.todo.repository

import com.nasiat.todo.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserPref {
    suspend fun saveUserModel(userModel: UserModel)

    fun getUserModel(): Flow<UserModel>
}