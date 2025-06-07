package com.example.comp319aproject2

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.comp319aproject2.TodoBoardTheme
import kotlinx.coroutines.flow.collectLatest

class DetailActivity : ComponentActivity() {
    private val viewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val itemId = intent.getLongExtra("itemId", -1L)
        if (itemId < 0) return finish()

        setContent {
            TodoBoardTheme {
                // Collect the item
                val itemState = viewModel.itemFlow(itemId)
                    .collectAsState(initial = null)

                itemState.value?.let { item ->
                    DetailScreen(
                        item = item,
                        onClose = { finish() },
                        onSave  = { updated ->
                            viewModel.updateItem(updated)
                            finish()
                        },
                        onDelete = {
                            // confirmation dialog
                            AlertDialog(
                                onDismissRequest = { /*no-op*/ },
                                title = { Text("Delete Item?") },
                                confirmButton = {
                                    TextButton(onClick = {
                                        viewModel.deleteItem(item)
                                        finish()
                                    }) { Text("Delete") }
                                },
                                dismissButton = {
                                    TextButton(onClick = { /*stay*/ }) { Text("Cancel") }
                                },
                                text = { Text("Are you sure you want to delete this item?") }
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
    onDelete: () -> Unit
) {
    var details by remember { mutableStateOf(item.details) }
    var isDone  by remember { mutableStateOf(item.isDone) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
            Text(
                text = "Created: ${item.createdAt}",
                style = MaterialTheme.typography.bodySmall
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isDone,
                onCheckedChange = { isDone = it }
            )
            Spacer(Modifier.width(8.dp))
            Text(item.title, style = MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = details,
            onValueChange = { details = it },
            label = { Text("Details") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
                onSave(item.copy(details = details, isDone = isDone))
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save")
        }
    }
}
