package com.example.project2

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {
    @Query("SELECT * FROM todo_items WHERE groupOwnerId = :arg0 ORDER BY createdAt")
    fun getItemsForGroup(arg0: Long): Flow<List<TodoItem>>

    @Query("SELECT * FROM todo_items WHERE itemId = :arg0")
    fun getItemById(arg0: Long): Flow<TodoItem>

    @Insert
    fun insertItem(item: TodoItem): Long

    @Update
    fun updateItem(item: TodoItem)

    @Delete
    fun deleteItem(item: TodoItem)
}

