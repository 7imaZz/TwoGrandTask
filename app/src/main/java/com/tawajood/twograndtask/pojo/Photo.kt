package com.tawajood.twograndtask.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey
    val id: Int,
    val albumId: Int,
    val title: String,
    val thumbnailUrl: String,
    val url: String
)