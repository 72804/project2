package com.example.comp319aproject2

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoGroupDao {
    @Query("SELECT * FROM todo_groups ORDER BY groupId")
    fun getAllGroups(): Flow<List<TodoGroup>>

    @Insert suspend fun insertGroup(group: TodoGroup): Long
    @Delete suspend fun deleteGroup(group: TodoGroup)
}