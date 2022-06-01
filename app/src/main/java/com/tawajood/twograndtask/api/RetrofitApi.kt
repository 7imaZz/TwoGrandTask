package com.tawajood.twograndtask.api

import com.tawajood.twograndtask.pojo.Album
import com.tawajood.twograndtask.pojo.Photo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {
    companion object{
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }

    @GET("albums")
    suspend fun getAlbums(
        @Query("userId") userId: Int
    ): Response<MutableList<Album>>

    @GET("photos")
    suspend fun getPhotos(
        @Query("albumId") albumId: Int
    ): Response<MutableList<Photo>>
}