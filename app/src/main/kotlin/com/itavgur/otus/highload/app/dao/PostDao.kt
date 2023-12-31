package com.itavgur.otus.highload.app.dao

import com.itavgur.otus.highload.app.domain.Post

interface PostDao {

    fun getPost(id: Int, userId: Int): Post?

    fun createPost(post: Post): Post

    fun updatePost(post: Post): Post

    fun deletePost(id: Int, userId: Int): Int

    fun feedPosts(offset: Int, limit: Int?, friendIds: Set<Int>): List<Post>

    fun getPostsByUserId(userId: Int): Set<Post>
}