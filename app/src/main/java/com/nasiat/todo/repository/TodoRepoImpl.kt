package com.nasiat.todo.repository

import com.nasiat.todo.database.ToDoEntity
import com.nasiat.todo.database.TodoDatabase
import kotlinx.coroutines.flow.Flow

class TodoRepoImpl (
    database: TodoDatabase
): ToDoRepo {

    private val dao = database.todoDao()

    override fun getTodos(): Flow<List<ToDoEntity>> = dao.getTodos()
    override suspend fun addTodo(todo: ToDoEntity) = dao.addTodo(todo)
    override suspend fun updateTodo(todo: ToDoEntity) = dao.updateTodo(todo)
    override suspend fun deleteTodo(todo: ToDoEntity) = dao.deleteTodo(todo)
}