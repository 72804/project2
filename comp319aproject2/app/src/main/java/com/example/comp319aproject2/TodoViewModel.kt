package com.example.comp319aproject2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoViewModel(app: Application) : AndroidViewModel(app) {

    /** Stream of one item, for DetailActivity */
    fun itemFlow(itemId: Long): Flow<TodoItem> = repo.getItemById(itemId)

    // build your DB once
    private val db = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "todo_board_db"
    )
        .fallbackToDestructiveMigration() // safe for dev
        .build()

    // repo instance
    private val repo = TodoRepository(db)

    /** Flows the list of groups */
    val groups: Flow<List<TodoGroup>> =
        repo.groupsFlow
            .stateIn(viewModelScope,
                started = kotlinx.coroutines.flow.SharingStarted.Lazily,
                initialValue = emptyList())

    /** Helper to get items for a group */
    fun itemsForGroup(groupId: Long): Flow<List<TodoItem>> =
        repo.itemsForGroup(groupId)
            .stateIn(viewModelScope,
                started = kotlinx.coroutines.flow.SharingStarted.Lazily,
                initialValue = emptyList())

    /** UI-triggered actions **/
    fun addGroup(name: String) = viewModelScope.launch {
        repo.addGroup(name)
    }
    fun deleteGroup(group: TodoGroup) = viewModelScope.launch {
        repo.deleteGroup(group)
    }
    fun addItem(groupId: Long, title: String) = viewModelScope.launch {
        repo.addItem(groupId, title)
    }
    fun updateItem(item: TodoItem) = viewModelScope.launch {
        repo.updateItem(item)
    }
    fun deleteItem(item: TodoItem) = viewModelScope.launch {
        repo.deleteItem(item)
    }
}
