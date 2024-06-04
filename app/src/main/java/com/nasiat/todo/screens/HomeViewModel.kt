package com.nasiat.todo.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasiat.todo.database.ToDoEntity
import com.nasiat.todo.repository.ToDoRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel: ViewModel(), KoinComponent {

    private val repo: ToDoRepo by inject()

    val todos: StateFlow<List<ToDoEntity>> = repo.getTodos().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )


    fun updateTodo(toDo: ToDoEntity) = viewModelScope.launch(Dispatchers.IO) {
        repo.updateTodo(toDo)
    }

    fun addTodo(toDo: ToDoEntity) = viewModelScope.launch(Dispatchers.IO) {
        repo.addTodo(toDo)
    }

    fun deleteTodo(toDo: ToDoEntity) = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteTodo(toDo)
    }

}