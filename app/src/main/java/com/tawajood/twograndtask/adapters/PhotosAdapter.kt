package com.tawajood.twograndtask.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tawajood.twograndtask.databinding.ImageItemBinding
import com.tawajood.twograndtask.pojo.Photo

class PhotosAdapter(private val onItemClick: OnItemClick):
    ListAdapter<Photo, PhotosAdapter.PhotoViewHolder>(PhotoComparator()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentImage = getItem(position)
        if (currentImage != null){
            holder.bind(currentImage)
        }

        holder.itemView.setOnClickListener {
            onItemClick.onItemClickListener(currentImage)
        }
    }

    class PhotoViewHolder(private val binding: ImageItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(image: Photo){
            Picasso.get().load(image.thumbnailUrl).into(binding.image)
        }
    }

    class PhotoComparator: DiffUtil.ItemCallback<Photo>(){
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
            oldItem == newItem
    }

    interface OnItemClick{
        fun onItemClickListener(photo: Photo)
    }
}
