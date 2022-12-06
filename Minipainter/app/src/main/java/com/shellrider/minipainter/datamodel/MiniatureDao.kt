package com.shellrider.minipainter.datamodel

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.StateFlow

@Dao
interface MiniatureDao {
    @Query("SELECT * FROM miniatures")
    fun getAllMiniatures(): LiveData<List<Miniature>>

    @Insert
    suspend fun insertMiniature(miniature: Miniature)
}