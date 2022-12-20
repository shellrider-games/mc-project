package com.shellrider.minipainter.datamodel

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.*


@Database(
    entities = [Image::class, Miniature::class, ProgressEntry::class],
    version = 5,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class MiniatureRoomDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun miniatureDao(): MiniatureDao
    abstract fun progressEntryDao(): ProgressEntryDao

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
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE miniatures" +
                            " ADD COLUMN description TEXT"
                )
            }
        }
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS progress_entries(" +
                            "progressEntryId INTEGER PRIMARY KEY NOT NULL," +
                            "imageId INTEGER NOT NULL," +
                            "miniatureId INTEGER NOT NULL," +
                            "description TEXT," +
                            "timestamp INTEGER NOT NULL" +
                            ");"
                )
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "INSERT INTO progress_entries " +
                            "SELECT primaryImageId AS progressEntryId, primaryImageId AS imageId , id AS miniatureId, " +
                            "'initial' AS description, lastUpdated AS timestamp " +
                            "FROM miniatures"
                )
            }
        }

        fun getDatabase(context: Context): MiniatureRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MiniatureRoomDatabase::class.java,
                    "miniature_database"
                ).addMigrations(
                    MIGRATION_1_2,
                    MIGRATION_2_3,
                    MIGRATION_3_4,
                    MIGRATION_4_5
                )
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


