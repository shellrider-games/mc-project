package com.shellrider.minipainter.datamodel

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface MiniatureDao {
    @Query("SELECT * FROM miniatures")
    fun getAllMiniatures(): LiveData<List<Miniature>>

    @Transaction
    @Query("SELECT * FROM miniatures WHERE id=:id")
    fun getMiniature(id: String): LiveData<MiniatureWithPrimaryImage>

    @Insert
    suspend fun insertMiniature(miniature: Miniature)

    @Transaction
    @Query("SELECT * FROM miniatures")
    fun getAllMiniaturesWithPrimaryImages(): LiveData<List<MiniatureWithPrimaryImage>>
}

