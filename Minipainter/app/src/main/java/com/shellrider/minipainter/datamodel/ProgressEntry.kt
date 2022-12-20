package com.shellrider.minipainter.datamodel

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.*

@Entity(tableName = "progress_entries")
data class ProgressEntry(
    @PrimaryKey(autoGenerate = true) val progressEntryId: Int,
    val imageId: Int,
    val miniatureId: Int,
    val description: String? = null,
    val timestamp: Date
)

data class ProgressEntryWithImage(
    @Embedded val progressEntry: ProgressEntry,
    @Relation(
        parentColumn = "imageId",
        entityColumn = "id"
    )
    val image: Image
)