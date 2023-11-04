package com.itavgur.otushighload.dao

import com.itavgur.otushighload.domain.Post

class PostDaoMock : PostDao {

    private val sqlTable: MutableSet<Post> = mutableSetOf()
    private var idSequence: Int = 1

    init {
        val mockPost01 = Post(id = 1, text = "Post number one", authorId = 1)
        val mockPost02 = Post(id = 2, text = "Post number two", authorId = 1)
        val mockPost03 = Post(id = 3, text = "Post number three", authorId = 2)
        createPost(mockPost01)
        createPost(mockPost02)
        createPost(mockPost03)
    }

    override fun getPost(id: Int, userId: Int): Post? {

        return sqlTable.firstOrNull { it.id == id }
    }

    override fun createPost(post: Post): Post {

        post.id = idSequence
        sqlTable.add(post)
        ++idSequence
        return post.clone()
    }

    override fun updatePost(post: Post): Post {

        sqlTable.firstOrNull { it.id == post.id }
            ?.let {
                sqlTable.remove(it)
                post.authorId = it.authorId
                sqlTable.add(post)
            }
        return post.clone()
    }

    override fun deletePost(id: Int, userId: Int): Int {
        var deletedEntries = 0
        sqlTable
            .firstOrNull { it.id == id }
            ?.let {
                sqlTable.remove(it)
                ++deletedEntries
            }
        return deletedEntries
    }

    override fun feedPosts(offset: Int, limit: Int?, friendIds: Set<Int>): List<Post> {

        return if (limit == null) {
            sqlTable.filter { friendIds.contains(it.authorId) }
                .drop(offset)
        } else {
            sqlTable.filter { friendIds.contains(it.authorId) }
                .drop(offset)
                .take(limit)
        }

    }

    override fun getPostsByUserId(userId: Int): Set<Post> {
        TODO("Not yet implemented")
    }

}