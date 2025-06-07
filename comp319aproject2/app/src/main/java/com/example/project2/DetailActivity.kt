package com.example.project2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class DetailActivity : ComponentActivity() {
    private val viewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val itemId = intent.getLongExtra("itemId", -1L)
        if (itemId < 0) finish().also { return }

        setContent {
            MaterialTheme {
                val item by viewModel.itemFlow(itemId).collectAsState(initial = null)
                item?.let { todo ->
                    DetailScreen(
                        item               = todo,
                        onClose            = { finish() },
                        onSave             = { updated ->
                            viewModel.updateItem(updated)
                            finish()
                        },
                        onDeleteConfirmed  = {
                            viewModel.deleteItemImmediate(todo)
                            finish()
                        },
                        onDoneChange       = { checked ->
                            viewModel.updateItemImmediate(
                                todo.copy(isDone = checked, details = todo.details)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DetailScreen(
    item: TodoItem,
    onClose: () -> Unit,
    onSave: (TodoItem) -> Unit,
    onDeleteConfirmed: () -> Unit,
    onDoneChange: (Boolean) -> Unit
) {
    var details         by remember { mutableStateOf(item.details) }
    var isDone          by remember { mutableStateOf(item.isDone) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
            Text(
                text  = "Created: ${item.createdAt}",
                style = MaterialTheme.typography.bodySmall
            )
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }

        Spacer(Modifier.height(16.dp))

        // Checkbox + title
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked        = isDone,
                onCheckedChange = { checked ->
                    isDone = checked
                    onDoneChange(checked)
                }
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text  = item.title,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(Modifier.height(16.dp))

        // Details input
        OutlinedTextField(
            value         = details,
            onValueChange = { details = it },
            label         = { Text("Details") },
            modifier      = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick  = { onSave(item.copy(details = details, isDone = isDone)) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save")
        }
    }

    // Delete confirmation
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title            = { Text("Delete Item?") },
            text             = { Text("Are you sure you want to delete this item?") },
            confirmButton    = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDeleteConfirmed()
                }) { Text("Delete") }
            },
            dismissButton    = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }
}
