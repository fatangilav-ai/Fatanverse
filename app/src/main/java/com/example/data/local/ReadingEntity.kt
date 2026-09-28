package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_progress")
data class ReadingProgressEntity(
    @PrimaryKey
    val chapterId: String,
    val scrollIndex: Int = 0,
    val scrollOffset: Int = 0,
    val progressPercent: Float = 0f,
    val lastReadTimestamp: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val chapterId: String,
    val arcTitle: String,
    val paragraphIndex: Int,
    val quote: String,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "reader_settings")
data class ReaderSettingEntity(
    @PrimaryKey
    val key: String,
    val value: String
)
