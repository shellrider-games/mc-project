package com.shellrider.minipainter.datamodel

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MiniatureDao {
    @Query("SELECT * FROM miniatures")
    fun getAllMiniatures(): LiveData<List<Miniature>>

    @Transaction
    @Query("SELECT * FROM miniatures WHERE id=:id")
    fun getMiniature(id: String): LiveData<MiniatureWithPrimaryImage>

    @Query("SELECT * FROM miniatures JOIN\n" +
            "(SELECT progress_entries.progressEntryId, progress_entries.imageId, progress_entries.miniatureId, progress_entries.description, progress_entries.timestamp FROM progress_entries,\n" +
            "( SELECT miniatureId, MAX(timestamp) AS timestamp FROM progress_entries GROUP BY miniatureId)\n" +
            "newest_entry WHERE progress_entries.miniatureId = newest_entry.miniatureId AND\n" +
            "progress_entries.timestamp = newest_entry.timestamp) progress_entry\n" +
            "ON miniatures.id = progress_entry.miniatureId")
    fun getMiniaturesWithNewestEntry(): LiveData<List<MiniatureWithLatestProgress>>

    @Insert
    suspend fun insertMiniature(miniature: Miniature): Long

    @Transaction
    @Query("SELECT * FROM miniatures")
    fun getAllMiniaturesWithPrimaryImages(): LiveData<List<MiniatureWithPrimaryImage>>

    @Delete
    suspend fun deleteMiniature(miniature: Miniature)

    @Update
    suspend fun updateMiniature(miniature: Miniature)

}

