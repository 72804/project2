package com.example.project2

import com.example.project2.AppDatabase
import kotlinx.coroutines.flow.Flow

class TodoRepository(private val db: AppDatabase) {
    // expose a Flow of all groups
    val groupsFlow: Flow<List<TodoGroup>>
        get() = db.todoGroupDao().getAllGroups()

    // single‐item stream
    fun getItemById(itemId: Long): Flow<TodoItem> = db.todoItemDao().getItemById(itemId)

    // expose a Flow of items for a given group
    fun itemsForGroup(groupId: Long): Flow<List<TodoItem>> =
        db.todoItemDao().getItemsForGroup(groupId)

    // group operations
    suspend fun addGroup(name: String): Long =
        db.todoGroupDao().insertGroup(TodoGroup(name = name))

    suspend fun deleteGroup(group: TodoGroup) =
        db.todoGroupDao().deleteGroup(group)

    // item operations
    suspend fun addItem(groupId: Long, title: String): Long =
        db.todoItemDao().insertItem(
            TodoItem(
                groupOwnerId = groupId,
                title = title
            )
        )

    suspend fun updateItem(item: TodoItem) =
        db.todoItemDao().updateItem(item)

    suspend fun deleteItem(item: TodoItem) =
        db.todoItemDao().deleteItem(item)
}
