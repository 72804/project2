package com.example.project2.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project2.DetailActivity
import com.example.project2.TodoItem
import com.example.project2.TodoViewModel
import com.example.project2.TodoGroup
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val timestampFormatter = DateTimeFormatter
    .ofPattern("MMM d, yyyy HH:mm")
    .withZone(ZoneId.systemDefault())

@Composable
fun MainScreen(viewModel: TodoViewModel = viewModel()) {
    val groups by viewModel.groups.collectAsState(initial = emptyList())
    var showAddGroup by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxHeight()
                .horizontalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            groups.forEach { group ->
                TodoGroupColumn(
                    groupId   = group.groupId,
                    groupName = group.name,
                    viewModel = viewModel
                )
            }
            AddGroupColumn { showAddGroup = true }
        }

        if (showAddGroup) {
            TextFieldDialog(
                title = "New Group",
                label = "Group name",
                onConfirm = {
                    viewModel.addGroup(it)
                    showAddGroup = false
                },
                onDismiss = { showAddGroup = false }
            )
        }
    }
}

@Composable
fun TodoGroupColumn(
    groupId: Long,
    groupName: String,
    viewModel: TodoViewModel
) {
    val context = LocalContext.current
    val items by viewModel
        .itemsForGroup(groupId)
        .collectAsState(initial = emptyList())

    var showAddItem by remember { mutableStateOf(false) }
    var showDeleteGroupDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .width(200.dp)
            .padding(4.dp)
            .fillMaxHeight()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = groupName,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1
            )
            IconButton(onClick = { showDeleteGroupDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Group")
            }
        }

        if (showDeleteGroupDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteGroupDialog = false },
                title = { Text("Delete Group?") },
                text = { Text("Delete \"$groupName\" and all its items?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteGroupImmediate(TodoGroup(groupId, groupName))
                        showDeleteGroupDialog = false
                    }) { Text("Delete") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteGroupDialog = false }) { Text("Cancel") }
                }
            )
        }

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            items.forEach { item ->
                TodoItemCard(
                    item,
                    onClick = {
                        context.startActivity(
                            Intent(context, DetailActivity::class.java)
                                .putExtra("itemId", item.itemId)
                        )
                    },
                    onCheckedChange = { checked ->
                        viewModel.updateItemImmediate(item.copy(isDone = checked))
                    }
                )
            }

            IconButton(
                onClick = { showAddItem = true },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }

        if (showAddItem) {
            TextFieldDialog(
                title = "New Item",
                label = "Title",
                onConfirm = {
                    viewModel.addItem(groupId, it)
                    showAddItem = false
                },
                onDismiss = { showAddItem = false }
            )
        }
    }
}

@Composable
fun TodoItemCard(
    item: TodoItem,
    onClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isDone,
                onCheckedChange = onCheckedChange
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (item.isDone) TextDecoration.LineThrough
                        else TextDecoration.None
                    )
                )
                Text(
                    text = timestampFormatter.format(item.createdAt),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun AddGroupColumn(onClick: () -> Unit) {
    Column(
        Modifier
            .width(200.dp)
            .padding(4.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = onClick) {
            Icon(Icons.Default.Add, contentDescription = "Add Group", modifier = Modifier.size(48.dp))
        }
        Text("New Group")
    }
}

// Reusable dialog for text input
@Composable
fun TextFieldDialog(
    title: String,
    label: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title            = { Text(title) },
        text             = {
            OutlinedTextField(
                value         = text,
                onValueChange = { text = it },
                label         = { Text(label) },
                singleLine    = true
            )
        },
        confirmButton    = {
            TextButton(onClick = { onConfirm(text) }) { Text("OK") }
        },
        dismissButton    = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
