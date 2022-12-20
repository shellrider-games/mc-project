package com.shellrider.minipainter.datamodel

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProgressEntryDao {
    @Query(
        "SELECT * FROM progress_entries, " +
                "( SELECT miniatureId, MAX(timestamp) AS timestamp FROM progress_entries GROUP BY miniatureId) " +
                "newest_entry WHERE progress_entries.miniatureId = newest_entry.miniatureId AND " +
                "progress_entries.timestamp = newest_entry.timestamp;"
    )
    fun getNewestEntries(): LiveData<List<ProgressEntry>>

    @Query("SELECT * FROM progress_entries")
    fun getProgressEntriesWithImages(): LiveData<ProgressEntryWithImage>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(progressEntry: ProgressEntry): Long
}