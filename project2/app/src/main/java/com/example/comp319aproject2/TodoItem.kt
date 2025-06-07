package com.example.project2

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "todo_items",
    foreignKeys = [
        ForeignKey(
            entity = TodoGroup::class,
            parentColumns = ["groupId"],
            childColumns = ["groupOwnerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val itemId: Long = 0,
    val groupOwnerId: Long,
    val title: String,
    val details: String = "",
    val createdAt: Instant = Instant.now(),
    val isDone: Boolean = false
)
