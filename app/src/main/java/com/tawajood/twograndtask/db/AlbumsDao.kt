package com.tawajood.twograndtask.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawajood.twograndtask.pojo.Album
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumsDao {
    @Query("SELECT * FROM albums")
    fun getAlbums(): Flow<MutableList<Album>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: MutableList<Album>)

    @Query("DELETE FROM albums")
    suspend fun deleteAllAlbums()
}