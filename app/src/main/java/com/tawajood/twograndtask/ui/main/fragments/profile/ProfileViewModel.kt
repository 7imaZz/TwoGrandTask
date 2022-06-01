package com.tawajood.twograndtask.ui.main.fragments.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawajood.twograndtask.db.AppDatabase
import com.tawajood.twograndtask.pojo.Album
import com.tawajood.twograndtask.pojo.User
import com.tawajood.twograndtask.repository.Repository
import com.tawajood.twograndtask.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    private val repository: Repository,
    db: AppDatabase
): ViewModel(){

    companion object{
        private const val TAG = "MainViewModel"
    }

    private val albumsDao = db.albumsDao()

    // This to act like we got user from server
    val user = MutableStateFlow(User())

    private val _albums = MutableStateFlow<Resource<MutableList<Album>>>(Resource.Idle())
    val albums = _albums.asStateFlow()



    init {
        getAlbums()
    }

    private fun getAlbums() = viewModelScope.launch {
        try {
            _albums.emit(Resource.Loading())
            val response = repository.getAlbums(user.value.id)
            if (response.isSuccessful && response.body() != null){
                _albums.emit(Resource.Success(response.body()!!))
                albumsDao.deleteAllAlbums()
                albumsDao.insertAlbums(response.body()!!)
            }else{
                Log.d(TAG, "getAlbums: ${response.message()}")
                albumsDao.getAlbums().collect {
                    Log.d(TAG, "getAlbums: $it")
                    if (it.isEmpty()){
                        _albums.emit(Resource.Error(message = response.message().toString()))
                    }else {
                        _albums.emit(Resource.Success(it))
                    }
                }
            }
        }catch (e: Exception){
            Log.d(TAG, "getAlbums: ${e.message}")
            albumsDao.getAlbums().collectLatest {
                Log.d(TAG, "getAlbums: $it")
                if (it.isEmpty()){
                    _albums.emit(Resource.Error(message = e.message.toString()))
                }else {
                    _albums.emit(Resource.Success(it))
                }
            }
        }
    }
}