package com.nasiat.todo.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Insert
    fun addTodo(todo: ToDoEntity)

    @Query("SELECT * from `todos`")
    fun getTodos(): Flow<List<ToDoEntity>>

    @Update
    fun updateTodo(todo: ToDoEntity)

    @Delete
    fun deleteTodo(todo: ToDoEntity)
}