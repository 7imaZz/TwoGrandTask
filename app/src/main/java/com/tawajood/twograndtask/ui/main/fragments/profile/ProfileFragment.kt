package com.tawajood.twograndtask.ui.main.fragments.profile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tawajood.twograndtask.R
import com.tawajood.twograndtask.adapters.AlbumsAdapter
import com.tawajood.twograndtask.databinding.FragmentProfileBinding
import com.tawajood.twograndtask.pojo.Album
import com.tawajood.twograndtask.pojo.User
import com.tawajood.twograndtask.ui.main.MainActivity
import com.tawajood.twograndtask.utils.Constants
import com.tawajood.twograndtask.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var parent: MainActivity
    private lateinit var adapter: AlbumsAdapter
    private lateinit var user: User
    private lateinit var albums: MutableList<Album>

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(requireView())
        parent = requireActivity() as MainActivity

        observeData()
        setupAdapter()
    }

    @SuppressLint("SetTextI18n")
    private fun setupUserData() {
        binding.nameTv.text = user.name
        binding.addressTv.text =
            "${user.address.street}, ${user.address.suite}, ${user.address.city},\n${user.address.zipcode}"
    }

    private fun setupAdapter() {
        adapter = AlbumsAdapter(object: AlbumsAdapter.OnItemClick{
            override fun onItemClickListener(position: Int) {
                parent.navController.navigate(R.id.action_profileFragment_to_photosFragment,
                    bundleOf(
                        Constants.ALBUM_ID to albums[position].id,
                        Constants.ALBUM_TITLE to albums[position].title
                    )
                )
            }
        })
        binding.albumsRv.adapter = adapter
    }

    private fun observeData() {
        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                user = it
                setupUserData()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.albums.collectLatest {
                binding.shimmer.stopShimmer()
                binding.shimmer.isVisible = false
                when (it) {
                    is Resource.Error -> Toast.makeText(
                        requireContext(),
                        it.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    is Resource.Idle -> {}
                    is Resource.Loading -> {
                        binding.shimmer.isVisible = true
                        binding.shimmer.startShimmer()
                    }
                    is Resource.Success -> {
                        albums = it.data!!
                        adapter.submitList(albums)
                    }
                }
            }
        }
    }
}