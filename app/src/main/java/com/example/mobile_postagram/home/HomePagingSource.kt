package com.example.mobile_postagram.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mobile_postagram.models.Post
import com.example.mobile_postagram.models.User

class HomePagingSource(val viewModel: HomeViewModel) : PagingSource<String, Pair<User, Post>>() {
    override fun getRefreshKey(state: PagingState<String, Pair<User, Post>>): String? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Pair<User, Post>> {
        var key = ""
        var user: User?
        var post: Post?
        viewModel.getPosts().collect {
            user = it.first
            post = it.second
        }
        return null!!
    }


}