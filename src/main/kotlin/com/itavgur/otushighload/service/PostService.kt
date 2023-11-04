package com.itavgur.otushighload.service

import com.itavgur.otushighload.dao.PostDao
import com.itavgur.otushighload.exception.PostNotFoundException
import com.itavgur.otushighload.web.dto.PostDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class PostService(
    @Autowired private val postDao: PostDao,
    @Autowired private val friendService: FriendService,
    @Autowired @Lazy private val self: PostService?
) {


    @Value(value = "\${cache.post-feed.limit:1000}")
    var feedLimit: Int = 0

    companion object {
        const val STATUS_FOR_NEW_POSTS = "OPEN"
        const val DEFAULT_FEED_OFFSET = 0
    }

    fun getPost(id: Int, userId: Int): PostDto {
        postDao.getPost(id, userId)?.let {
            return PostDto.from(it)
        }
        throw PostNotFoundException("post with id $id missed")
    }

    fun savePost(request: PostDto, userId: Int): PostDto {

        request.id?.let {
            postDao.getPost(request.id!!, userId)?.let {
                request.authorId = userId
                evictCacheForFriends(userId)
                return PostDto.from(postDao.updatePost(request.toPost()))
            }
            throw PostNotFoundException("post with id ${request.id} missed")
        }
        request.authorId = userId
        val postToCreate = postDao.createPost(request.toPost())
        postToCreate.status = STATUS_FOR_NEW_POSTS
        evictCacheForFriends(userId)
        return PostDto.from(postToCreate)
    }

    fun deletePost(id: Int, userId: Int) {

        postDao.getPost(id, userId)?.let {
            if (postDao.deletePost(id, userId) > 0) {
                evictCacheForFriends(id)
                return
            }
        }
        throw PostNotFoundException("post with id ${id} missed")
    }

    @Cacheable("post-feed", key = "{#userId}")
    fun feedPosts(offset: Int = DEFAULT_FEED_OFFSET, limit: Int = feedLimit, userId: Int): List<PostDto> {

        val friends = friendService.findFriendsOf(userId)

        return if (friends.isNotEmpty()) {
            postDao.feedPosts(offset, limit, friends).map { PostDto.from(it) }.toList()
        } else {
            emptyList()
        }
    }

    private fun evictCacheForFriends(userId: Int) {
        friendService.findSubscribersOf(userId).forEach {
            self!!.evictFeedCacheForUser(it)
        }
    }

    @CacheEvict("post-feed", key = "{#userId}")
    fun evictFeedCacheForUser(userId: Int) {
        println("")
        //nothing here, just for annotation
    }

}