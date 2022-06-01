package com.tawajood.twograndtask.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class Album(
    @PrimaryKey
    val id: Int,
    val userId: Int,
    val title: String
)
