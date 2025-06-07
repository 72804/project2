package com.example.project2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_groups")
data class TodoGroup(
    @PrimaryKey(autoGenerate = true)
    val groupId: Long = 0,
    val name: String
)
