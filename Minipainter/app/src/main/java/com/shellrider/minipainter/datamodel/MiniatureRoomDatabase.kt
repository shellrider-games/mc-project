package com.shellrider.minipainter.datamodel

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Image::class, Miniature::class], version = 1, exportSchema = true)
public abstract class MiniatureRoomDatabase : RoomDatabase() {
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