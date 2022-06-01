package com.tawajood.twograndtask.ui.main.fragments.photos

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawajood.twograndtask.db.AppDatabase
import com.tawajood.twograndtask.pojo.Photo
import com.tawajood.twograndtask.repository.Repository
import com.tawajood.twograndtask.utils.Constants
import com.tawajood.twograndtask.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel
@Inject
constructor(
    private val repository: Repository,
    savedStateHandle: SavedStateHandle,
    db: AppDatabase
): ViewModel() {

    companion object {
        private const val TAG = "PhotosViewModel"
    }

    private val photosDao = db.photosDao()
    private val albumId = savedStateHandle.getLiveData<Int>(Constants.ALBUM_ID)

    private val _photos = MutableStateFlow<Resource<MutableList<Photo>>>(Resource.Idle())
    val photos = _photos.asStateFlow()

    init {
        getPhotos()
    }

    private fun getPhotos() = viewModelScope.launch {
        try {
            _photos.emit(Resource.Loading())
            val response = repository.getPhotos(albumId.value!!)
            if (response.isSuccessful && response.body() != null) {
                _photos.emit(Resource.Success(response.body()!!))
                photosDao.deleteAllPhotosByAlbumId(albumId.value!!)
                photosDao.insertPhotos(response.body()!!)
            } else {
                _photos.emit(Resource.Error(message = response.message().toString()))
            }
        } catch (e: Exception) {
            photosDao.getPhotos(albumId.value!!).collectLatest {
                Log.d(TAG, "getPhotos: $it")
                if (it.isEmpty()) {
                    _photos.emit(Resource.Error(message = e.message.toString()))
                } else {
                    _photos.emit(Resource.Success(it))
                }
            }
        }
    }

}