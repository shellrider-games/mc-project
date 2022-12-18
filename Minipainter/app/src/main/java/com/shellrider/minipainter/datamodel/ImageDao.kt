package com.shellrider.minipainter.datamodel

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDao {
    @Query("SELECT * FROM image_table")
    fun getAllImages(): LiveData<List<Image>>

    @Query("SELECT * FROM image_table WHERE id = :id")
    fun getImageById(id: String): LiveData<Image>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(image: Image): Long
}