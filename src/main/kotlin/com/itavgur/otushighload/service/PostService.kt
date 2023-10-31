package com.itavgur.otushighload.service

import com.itavgur.otushighload.dao.PostDao
import com.itavgur.otushighload.exception.CredentialException
import com.itavgur.otushighload.exception.PostNotFoundException
import com.itavgur.otushighload.exception.UserNotFoundException
import com.itavgur.otushighload.web.dto.PostDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PostService(
    @Autowired private val postDao: PostDao,
    @Autowired private val credentialService: CredentialService,
    @Autowired private val friendService: FriendService
) {
    fun getPost(id: Int, token: String): PostDto {
        postDao.getPost(id)
            ?.let {
                checkAccessToPost(it.authorId!!, token)
                return PostDto.from(it)
            }
        throw PostNotFoundException("post with id $id missed")
    }

    fun savePost(request: PostDto, token: String): PostDto {

        request.id?.let {
            postDao.getPost(request.id!!)
                ?.let {
                    checkAccessToPost(it.authorId!!, token)
                    return PostDto.from(postDao.updatePost(request.toPost()))
                }
            throw PostNotFoundException("post with id ${request.id} missed")
        }
        val credential = credentialService.getCredentialByToken(token)
        request.authorId = credential.userId
        return PostDto.from(postDao.createPost(request.toPost()))
    }

    fun deletePost(id: Int, token: String) {

        postDao.getPost(id)
            ?.let {
                checkAccessToPost(it.authorId!!, token)
                if (postDao.deletePost(id) > 0) return
            }
        throw PostNotFoundException("post with id ${id} missed")

    }

    fun feedPosts(offset: Int, limit: Int, token: String): List<PostDto> {

        val credential = credentialService.getCredentialByToken(token)

        val friends = friendService.findFriendsOf(credential.userId!!)

        return postDao.feedPosts(offset, limit, friends).map { PostDto.from(it) }.toList()
    }

    @Throws(UserNotFoundException::class)
    private fun checkAccessToPost(authorId: Int, token: String): Int {

        try {
            val credential = credentialService.getCredentialByToken(token)
            if (credential.userId != authorId) {
                throw PostNotFoundException("userId from this request doesn't match to userId from request")
            }
            return credential.userId!!
        } catch (ex: CredentialException) {
            throw PostNotFoundException("user with this token isn't found")
        }
    }

}