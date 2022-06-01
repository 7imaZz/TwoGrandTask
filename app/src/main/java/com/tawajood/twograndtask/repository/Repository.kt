package com.tawajood.twograndtask.repository

import com.tawajood.twograndtask.api.RetrofitApi
import com.tawajood.twograndtask.db.AppDatabase
import javax.inject.Inject

class Repository
@Inject
constructor(private val api: RetrofitApi){

    suspend fun getAlbums(userId: Int) = api.getAlbums(userId)

    suspend fun getPhotos(albumId: Int) = api.getPhotos(albumId)
}