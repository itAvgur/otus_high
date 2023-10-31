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

    override fun getPost(id: Int): Post? {

        return sqlTable.firstOrNull { it.id == id }
    }

    override fun createPost(request: Post): Post {

        request.id = idSequence
        sqlTable.add(request)
        ++idSequence
        return request.clone()
    }

    override fun updatePost(request: Post): Post {

        sqlTable.firstOrNull { it.id == request.id }
            ?.let {
                sqlTable.remove(it)
                request.authorId = it.authorId
                sqlTable.add(request)
            }
        return request.clone()
    }

    override fun deletePost(id: Int): Int {
        var deletedEntries: Int = 0
        sqlTable
            .firstOrNull { it.id == id }
            ?.let {
                sqlTable.remove(it)
                ++deletedEntries
            }
        return deletedEntries
    }

    override fun feedPosts(offset: Int, limit: Int, friendIds: Set<Int>): List<Post> {

        return sqlTable.filter { friendIds.contains(it.authorId) }
            .drop(offset)
            .take(limit)
    }

}