package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ReadingProgressEntity::class,
        BookmarkEntity::class,
        ReaderSettingEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FatanDatabase : RoomDatabase() {

    abstract fun fatanDao(): FatanDao

    companion object {
        @Volatile
        private var INSTANCE: FatanDatabase? = null

        fun getDatabase(context: Context): FatanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FatanDatabase::class.java,
                    "fatanverse_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
