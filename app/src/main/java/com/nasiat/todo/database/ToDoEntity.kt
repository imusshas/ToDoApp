package com.nasiat.todo.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date

@Entity("todos")
data class ToDoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("sub_title")
    val subTitle: String,
    @ColumnInfo("done")
    val done: Boolean = false,
    @ColumnInfo("added")
    val added: Long = System.currentTimeMillis(),
)

val ToDoEntity.addDate: String get() = SimpleDateFormat("dd/MM/yyyy hh:mm").format(Date(added))
