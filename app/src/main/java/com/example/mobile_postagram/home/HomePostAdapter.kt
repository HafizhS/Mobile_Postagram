package com.example.mobile_postagram.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobile_postagram.databinding.PostItemBinding
import com.example.mobile_postagram.models.Post
import com.example.mobile_postagram.models.User

class HomePostAdapter(val data: MutableList<Pair<User, Post>> = mutableListOf()) :
    RecyclerView.Adapter<HomePostAdapter.HomePostViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomePostAdapter.HomePostViewHolder {
        return HomePostAdapter.HomePostViewHolder(
            PostItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomePostAdapter.HomePostViewHolder, position: Int) {
        val pair = data[position]
        holder.bind(pair)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addData(pair: Pair<User, Post>) {
        data.add(pair)
        notifyItemInserted(data.size)
    }

    class HomePostViewHolder(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pair: Pair<User, Post>) {
            val (user, post) = pair
            Glide.with(binding.root)
                .load(post.imageUrl)
                .fitCenter()
                .into(binding.postImage)

            Glide.with(binding.root)
                .load(user.avatar)
                .fitCenter()
                .into(binding.postAvatar)

            binding.tvUploaderName.text = user.displayName
        }
    }
}
