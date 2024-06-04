package com.nasiat.todo.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nasiat.todo.database.ToDoEntity
import com.nasiat.todo.database.addDate

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val todos = viewModel.todos.collectAsState().value

    val (dialogOpen, setDialogOpen) = rememberSaveable { mutableStateOf(false) }

    if (dialogOpen) {

        val (title, setTitle) = rememberSaveable { mutableStateOf("") }
        val (subtitle, setSubtitle) = rememberSaveable { mutableStateOf("") }

        val (titleError, setTitleError) = rememberSaveable { mutableStateOf<String?>(null) }
        val (subtitleError, setSubTitleError) = rememberSaveable { mutableStateOf<String?>(null) }

        Dialog(onDismissRequest = { setDialogOpen(false) }) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { setTitle(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Title") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        unfocusedTextColor = Color.White,
                        errorTextColor = Color.White,
                        cursorColor = Color.White,
                        errorLabelColor = Color.White,
                        errorCursorColor = Color.White
                    ),
                    isError = titleError != null,
                    supportingText = {
                        Text(
                            text = titleError.orEmpty(),
                            color = MaterialTheme.colorScheme.onError
                        )
                    }
                )
                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { setSubtitle(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Sub Title") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        unfocusedTextColor = Color.White,
                        errorTextColor = Color.White,
                        cursorColor = Color.White,
                        errorLabelColor = Color.White,
                        errorCursorColor = Color.White
                    ),
                    isError = subtitleError != null,
                    supportingText = {
                        Text(
                            text = subtitleError.orEmpty(),
                            color = MaterialTheme.colorScheme.onError
                        )
                    }
                )
                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = {
                        if (title.isNotBlank() && subtitle.isNotBlank()) {
                            viewModel.addTodo(
                                ToDoEntity(
                                    title = title,
                                    subTitle = subtitle
                                )
                            )
                            setDialogOpen(false)
                        } else {
                            if (title.isBlank()) setTitleError("Title can't be empty")
                            if (subtitle.isBlank()) setSubTitleError("Subtitle can't be empty")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Submit")
                }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.secondary,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { setDialogOpen(true) },
                contentColor = Color.White,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add ToDo")
            }
        }
    ) { paddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = todos.isEmpty(),
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Text(text = "No Todos Yet!", color = Color.White, fontSize = 22.sp)
            }
            AnimatedVisibility(
                visible = todos.isNotEmpty(),
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(todos.sortedBy { it.done }, key = { it.id }) { todo ->
                        TodoItem(onClick = { viewModel.updateTodo(todo.copy(done = !todo.done)) }, onDelete = { viewModel.deleteTodo(todo) }, todo = todo)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyItemScope.TodoItem(
    onClick: () -> Unit,
    onDelete: () -> Unit,
    todo: ToDoEntity
) {

    val color by animateColorAsState(
        targetValue = if (todo.done) Color(0xff24d65f) else Color(0xffff6363),
        animationSpec = tween(500),
        label = ""
    )

    Box(modifier = Modifier.fillMaxWidth().animateItemPlacement(), contentAlignment = Alignment.Center) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(color)
                .clickable { onClick() }
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Row(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row {
                        AnimatedVisibility(
                            visible = todo.done,
                            enter = scaleIn() + fadeIn(),
                            exit = scaleOut() + fadeOut()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = color
                            )
                        }
                    }

                    Row {
                        AnimatedVisibility(
                            visible = !todo.done,
                            enter = scaleIn() + fadeIn(),
                            exit = scaleOut() + fadeOut()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = color
                            )
                        }
                    }
                }

                Column {
                    Text(
                        text = todo.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Text(
                        text = todo.subTitle,
                        fontSize = 12.sp,
                        color = Color(0xffebebeb)
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.End
            ) {

                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(4.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.clickable { onDelete() })
                }

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = todo.addDate,
                    color = Color(0xffebebeb),
                    fontSize = 10.sp
                )
            }
        }

    }


}