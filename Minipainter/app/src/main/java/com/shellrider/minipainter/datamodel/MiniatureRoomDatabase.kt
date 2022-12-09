package com.shellrider.minipainter.datamodel

import android.content.Context
import androidx.room.*
import java.util.*

@Database(entities = [Image::class, Miniature::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class MiniatureRoomDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun miniatureDao(): MiniatureDao

    companion object {
        @Volatile
        private var INSTANCE: MiniatureRoomDatabase? = null

        fun getDatabase(context: Context): MiniatureRoomDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MiniatureRoomDatabase::class.java,
                    "miniature_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return if (date == null) null else date.time
    }

}