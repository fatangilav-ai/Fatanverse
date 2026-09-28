package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FatanDao {

    @Query("SELECT * FROM reading_progress")
    fun getAllProgress(): Flow<List<ReadingProgressEntity>>

    @Query("SELECT * FROM reading_progress WHERE chapterId = :chapterId LIMIT 1")
    fun getProgressByChapterId(chapterId: String): Flow<ReadingProgressEntity?>

    @Query("SELECT * FROM reading_progress WHERE chapterId = :chapterId LIMIT 1")
    suspend fun getProgressImmediate(chapterId: String): ReadingProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: ReadingProgressEntity)

    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmarks WHERE chapterId = :chapterId ORDER BY paragraphIndex ASC")
    fun getBookmarksByChapter(chapterId: String): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity): Long

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmarkById(id: Long)

    @Query("SELECT * FROM reader_settings WHERE `key` = :key LIMIT 1")
    suspend fun getSetting(key: String): ReaderSettingEntity?

    @Query("SELECT * FROM reader_settings")
    fun getAllSettings(): Flow<List<ReaderSettingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSetting(setting: ReaderSettingEntity)
}
