package com.example.mobile_postagram.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobile_postagram.databinding.ProfilePostItemBinding
import com.example.mobile_postagram.models.Post

class ProfilePostAdapter(private val data: MutableList<Post> = mutableListOf()) :
    RecyclerView.Adapter<ProfilePostAdapter.ProfilePostViewHolder>() {

    class ProfilePostViewHolder(val binding: ProfilePostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            Glide.with(binding.root)
                .load(post.imageUrl)
                .centerCrop()
                .into(binding.ivImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePostViewHolder {
        return ProfilePostViewHolder(
            ProfilePostItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProfilePostViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun addPost(post: Post) {
        data.add(post)
        notifyItemRangeInserted(0, data.size)
    }

    override fun getItemCount(): Int {
        return data.size
    }

}
