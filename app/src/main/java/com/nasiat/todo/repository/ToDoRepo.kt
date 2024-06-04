package com.nasiat.todo.repository

import com.nasiat.todo.database.ToDoEntity
import kotlinx.coroutines.flow.Flow

interface ToDoRepo {
    fun getTodos(): Flow<List<ToDoEntity>>
    suspend fun addTodo(todo: ToDoEntity)
    suspend fun updateTodo(todo: ToDoEntity)
    suspend fun deleteTodo(todo: ToDoEntity)
}