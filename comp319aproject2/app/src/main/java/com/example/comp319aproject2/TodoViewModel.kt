package com.example.project2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoViewModel(app: Application) : AndroidViewModel(app) {

    private val db   = AppDatabase.getInstance(app)
    private val repo = TodoRepository(db)

    val groups: Flow<List<TodoGroup>> = repo.groupsFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun itemsForGroup(groupId: Long): Flow<List<TodoItem>> =
        repo.itemsForGroup(groupId)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun itemFlow(itemId: Long): Flow<TodoItem> =
        repo.getItemById(itemId)

    // Async actions
    fun addGroup(name: String) = viewModelScope.launch { repo.addGroup(name) }
    fun deleteGroup(group: TodoGroup) = viewModelScope.launch { repo.deleteGroup(group) }
    fun addItem(groupId: Long, title: String) = viewModelScope.launch { repo.addItem(groupId, title) }
    fun updateItem(item: TodoItem) = viewModelScope.launch { repo.updateItem(item) }
    fun deleteItem(item: TodoItem) = viewModelScope.launch { repo.deleteItem(item) }

    // Immediate (blocking) helpers
    fun deleteItemImmediate(item: TodoItem) {
        db.todoItemDao().deleteItem(item)
    }
    fun deleteGroupImmediate(group: TodoGroup) {
        db.todoGroupDao().deleteGroup(group)
    }
    // immediate update
    fun updateItemImmediate(item: TodoItem) {
        db.todoItemDao().updateItem(item)
    }
}

