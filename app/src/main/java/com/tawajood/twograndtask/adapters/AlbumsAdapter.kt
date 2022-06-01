package com.tawajood.twograndtask.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tawajood.twograndtask.databinding.AlbumItemBinding
import com.tawajood.twograndtask.pojo.Album

class AlbumsAdapter(private val onItemClick: OnItemClick):
    ListAdapter<Album, AlbumsAdapter.AlbumViewHolder>(AlbumComparator()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumViewHolder {
        val binding = AlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val currentAlbum = getItem(position)
        if (currentAlbum != null){
            holder.bind(currentAlbum)
        }

        holder.itemView.setOnClickListener {
            onItemClick.onItemClickListener(position)
        }
    }

    class AlbumViewHolder(private val binding: AlbumItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album){
            binding.titleTv.text = album.title
        }
    }

    class AlbumComparator: DiffUtil.ItemCallback<Album>(){
        override fun areItemsTheSame(oldItem: Album, newItem: Album) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Album, newItem: Album) =
            oldItem == newItem
    }

    interface OnItemClick{
        fun onItemClickListener(position: Int)
    }
}

