package com.nasiat.todo.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ToDoEntity::class], version = 1)
abstract class TodoDatabase: RoomDatabase() {

    abstract fun todoDao(): ToDoDao
}