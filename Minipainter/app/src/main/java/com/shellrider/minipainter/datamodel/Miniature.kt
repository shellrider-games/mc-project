package com.shellrider.minipainter.datamodel

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.OffsetDateTime
import java.util.Date

@Entity(tableName = "miniatures")
data class Miniature(
    @PrimaryKey val id: Int,
    val name: String,
    val lastUpdated: Date? = null,
    val primaryImageId: Int
)

data class MiniatureWithPrimaryImage(
    @Embedded val miniature: Miniature,
    @Relation(
        parentColumn = "primaryImageId",
        entityColumn = "id"
    )
    val image: Image

)
