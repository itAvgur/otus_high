package com.itavgur.otushighload.dao

import com.itavgur.otushighload.domain.Post

interface PostDao {

    fun getPost(id: Int): Post?

    fun createPost(request: Post): Post

    fun updatePost(request: Post): Post

    fun deletePost(id: Int): Int

    fun feedPosts(offset: Int, limit: Int, friendIds: Set<Int>): List<Post>

}