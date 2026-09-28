package com.example.data.repository

import com.example.data.local.BookmarkEntity
import com.example.data.local.FatanDao
import com.example.data.local.ReaderSettingEntity
import com.example.data.local.ReadingProgressEntity
import kotlinx.coroutines.flow.Flow

class FatanRepository(private val dao: FatanDao) {

    fun getAllProgress(): Flow<List<ReadingProgressEntity>> = dao.getAllProgress()

    fun getProgress(chapterId: String): Flow<ReadingProgressEntity?> =
        dao.getProgressByChapterId(chapterId)

    suspend fun getProgressImmediate(chapterId: String): ReadingProgressEntity? =
        dao.getProgressImmediate(chapterId)

    suspend fun saveProgress(chapterId: String, scrollIndex: Int, scrollOffset: Int, percent: Float, isCompleted: Boolean) {
        dao.saveProgress(
            ReadingProgressEntity(
                chapterId = chapterId,
                scrollIndex = scrollIndex,
                scrollOffset = scrollOffset,
                progressPercent = percent,
                lastReadTimestamp = System.currentTimeMillis(),
                isCompleted = isCompleted
            )
        )
    }

    fun getAllBookmarks(): Flow<List<BookmarkEntity>> = dao.getAllBookmarks()

    fun getBookmarksByChapter(chapterId: String): Flow<List<BookmarkEntity>> =
        dao.getBookmarksByChapter(chapterId)

    suspend fun addBookmark(chapterId: String, arcTitle: String, paragraphIndex: Int, quote: String, note: String): Long {
        return dao.insertBookmark(
            BookmarkEntity(
                chapterId = chapterId,
                arcTitle = arcTitle,
                paragraphIndex = paragraphIndex,
                quote = quote,
                note = note,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun removeBookmark(id: Long) {
        dao.deleteBookmarkById(id)
    }

    suspend fun getSetting(key: String, defaultValue: String): String {
        return dao.getSetting(key)?.value ?: defaultValue
    }

    suspend fun setSetting(key: String, value: String) {
        dao.saveSetting(ReaderSettingEntity(key = key, value = value))
    }

    fun getAllSettings(): Flow<List<ReaderSettingEntity>> = dao.getAllSettings()
}
