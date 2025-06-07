package com.example.project2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoGroupDao {
    @Query("SELECT * FROM todo_groups ORDER BY groupId")
    fun getAllGroups(): Flow<List<TodoGroup>>

    @Insert
    fun insertGroup(group: TodoGroup): Long

    @Delete
    fun deleteGroup(group: TodoGroup)
}
