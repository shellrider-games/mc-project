package com.shellrider.minipainter.datamodel

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.*

@Entity(tableName = "miniatures")
data class Miniature(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val lastUpdated: Date? = null,
    val primaryImageId: Int,
    val progress: Float = 0.0f
)

data class MiniatureWithPrimaryImage(
    @Embedded val miniature: Miniature,
    @Relation(
        parentColumn = "primaryImageId",
        entityColumn = "id"
    )
    val image: Image

)
