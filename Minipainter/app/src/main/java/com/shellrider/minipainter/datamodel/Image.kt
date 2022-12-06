package com.shellrider.minipainter.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_table")
data class Image (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val filename: String
)