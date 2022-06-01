package com.tawajood.twograndtask.ui.main.fragments.photos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tawajood.twograndtask.R
import com.tawajood.twograndtask.adapters.PhotosAdapter
import com.tawajood.twograndtask.databinding.FragmentPhotosBinding
import com.tawajood.twograndtask.pojo.Photo
import com.tawajood.twograndtask.ui.main.fragments.image_sheet.ImageSheet
import com.tawajood.twograndtask.utils.Constants
import com.tawajood.twograndtask.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*


@AndroidEntryPoint
class PhotosFragment : Fragment(R.layout.fragment_photos) {

    private lateinit var binding: FragmentPhotosBinding
    private lateinit var adapter: PhotosAdapter
    private lateinit var photos: MutableList<Photo>
    private lateinit var title: String

    private val viewModel: PhotosViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPhotosBinding.bind(requireView())
        title = requireArguments().getString(Constants.ALBUM_TITLE, "")

        binding.titleTv.text = title
        setupAdapter()
        observeData()
        filter()
    }

    private fun setupAdapter() {
        adapter = PhotosAdapter(object : PhotosAdapter.OnItemClick {
            override fun onItemClickListener(photo: Photo) {
                ImageSheet.newInstance(photo.thumbnailUrl)
                    .show(childFragmentManager, ImageSheet::class.java.canonicalName)
            }
        })
        binding.imagesRv.adapter = adapter
    }

    private fun observeData() {
        lifecycleScope.launchWhenStarted {
            viewModel.photos.collectLatest {
                binding.shimmer.stopShimmer()
                binding.shimmer.isVisible = false
                when (it) {
                    is Resource.Error -> Toast.makeText(
                        requireContext(),
                        it.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    is Resource.Idle -> {
                    }
                    is Resource.Loading -> {
                        binding.shimmer.isVisible = true
                        binding.shimmer.startShimmer()
                    }
                    is Resource.Success -> {
                        photos = it.data!!
                        adapter.submitList(photos)
                    }
                }
            }
        }
    }

    private fun filter(){
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(s: String?): Boolean {
                val filteredPhotos = mutableListOf<Photo>()
                for (photo in photos) {
                    if (photo.title.lowercase(Locale.ROOT).contains(s!!.lowercase(Locale.ROOT))) {
                        filteredPhotos.add(photo)
                    }
                }
                adapter.submitList(filteredPhotos)
                return true
            }

        })
    }
}