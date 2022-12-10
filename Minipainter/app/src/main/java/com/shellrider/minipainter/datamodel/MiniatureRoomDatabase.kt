package com.shellrider.minipainter.datamodel

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.*


@Database(entities = [Image::class, Miniature::class], version = 2, exportSchema = true)
@TypeConverters(Converters::class)
abstract class MiniatureRoomDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun miniatureDao(): MiniatureDao

    companion object {
        @Volatile
        private var INSTANCE: MiniatureRoomDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE miniatures" +
                            " ADD COLUMN progress REAL NOT NULL DEFAULT 0.0"
                )
            }
        }

        fun getDatabase(context: Context): MiniatureRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MiniatureRoomDatabase::class.java,
                    "miniature_database"
                ).addMigrations(MIGRATION_1_2)
                    .build()
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


