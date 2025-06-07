package com.example.comp319aproject2

import androidx.room.TypeConverter
import java.time.Instant

object Converters {
    @TypeConverter fun fromInstant(value: Instant) = value.toEpochMilli()
    @TypeConverter fun toInstant(millis: Long) = Instant.ofEpochMilli(millis)
}