package com.tawajood.twograndtask.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tawajood.twograndtask.pojo.Album
import com.tawajood.twograndtask.pojo.Photo

@Database(
    entities = [Album::class, Photo::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun albumsDao(): AlbumsDao
    abstract fun photosDao(): PhotosDao

}