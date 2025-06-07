package com.example.comp319aproject2

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {
    @Query("SELECT * FROM todo_items WHERE groupOwnerId = :groupId ORDER BY createdAt")
    fun getItemsForGroup(groupId: Long): Flow<List<TodoItem>>

    @Query("SELECT * FROM todo_items WHERE itemId = :itemId")
    fun getItemById(itemId: Long): Flow<TodoItem>

    @Insert suspend fun insertItem(item: TodoItem): Long
    @Update suspend fun updateItem(item: TodoItem)
    @Delete suspend fun deleteItem(item: TodoItem)
}