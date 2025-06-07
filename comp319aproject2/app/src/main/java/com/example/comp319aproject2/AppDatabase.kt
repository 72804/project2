package com.example.comp319aproject2

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TodoGroup::class, TodoItem::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class) // if you add Instant→Long converters
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoGroupDao(): TodoGroupDao
    abstract fun todoItemDao(): TodoItemDao
}