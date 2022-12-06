package com.shellrider.minipainter.datamodel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDao {
    @Query("SELECT * FROM image_table")
    fun getAllImages(): List<Image>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(image: Image)
}