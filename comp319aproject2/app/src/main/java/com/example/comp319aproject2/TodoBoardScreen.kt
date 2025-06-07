package com.example.comp319aproject2

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comp319aproject2.TodoItem
import com.example.comp319aproject2.TodoViewModel

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
            // For each existing group
            groups.forEach { group ->
                TodoGroupColumn(
                    groupId   = group.groupId,
                    groupName = group.name,
                    viewModel = viewModel
                )
            }

            // “+ Group” column
            AddGroupColumn { showAddGroup = true }
        }

        // Dialog to add new group
        if (showAddGroup) {
            TextFieldDialog(
                title = "New Group",
                label = "Group name",
                onConfirm = { name ->
                    viewModel.addGroup(name)
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
    val items by viewModel
        .itemsForGroup(groupId)
        .collectAsState(initial = emptyList())

    var showAddItem by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .width(200.dp)
            .padding(4.dp)
            .fillMaxHeight()
    ) {
        // Header (fixed)
        Text(
            text = groupName,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            maxLines = 1
        )

        // Items (scrollable)
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            items.forEach { item ->
                TodoItemCard(item, onClick = {
                    // TODO: launch detail activity in Step 5
                }, onCheckedChange = { checked ->
                    viewModel.updateItem(item.copy(isDone = checked))
                })
            }

            // “+ Item” button
            IconButton(
                onClick = { showAddItem = true },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }

        // Add-item dialog
        if (showAddItem) {
            TextFieldDialog(
                title = "New Item",
                label = "Title",
                onConfirm = { title ->
                    viewModel.addItem(groupId, title)
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
            .clickable { onClick() },
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
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge
            )
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
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(label) },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(text) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
