package com.shellrider.minipainter.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "miniatures")
data class Miniature(
    @PrimaryKey val id: Int,
    val name: String
)
