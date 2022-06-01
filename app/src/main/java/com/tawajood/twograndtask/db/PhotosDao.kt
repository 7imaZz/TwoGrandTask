package com.tawajood.twograndtask.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawajood.twograndtask.pojo.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotosDao {
    @Query("SELECT * FROM photos WHERE albumId = :albumId")
    fun getPhotos(albumId: Int): Flow<MutableList<Photo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: MutableList<Photo>)

    @Query("DELETE FROM photos WHERE albumId = :albumId")
    suspend fun deleteAllPhotosByAlbumId(albumId: Int)
}