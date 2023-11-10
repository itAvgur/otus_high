package com.itavgur.otus.highload.app.service

import com.itavgur.otus.highload.app.dao.FriendDao
import com.itavgur.otus.highload.app.domain.Friend
import com.itavgur.otus.highload.app.exception.CredentialException
import com.itavgur.otus.highload.app.exception.InvalidRequestException
import com.itavgur.otus.highload.app.exception.UserNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class FriendService(
    @Autowired private val friendDao: FriendDao,
    @Autowired private val userService: UserService,
    @Lazy @Autowired private val postService: PostService,
    @Autowired private val credentialService: CredentialService
) {

    fun getFriend(friendId: Int, token: String): Friend {
        try {
            val userId = credentialService.getCredentialByToken(token).userId!!

            friendDao.getFriend(userId, friendId)
                ?.let { return it }
            throw InvalidRequestException("users ain't found as friends")
        } catch (ex: CredentialException) {
            throw UserNotFoundException("user_id not found")
        }

    }

    fun setFriend(friendId: Int, token: String) {

        try {
            val userId = credentialService.getCredentialByToken(token).userId!!

            userService.getUser(friendId)

            if (friendDao.getFriend(userId, friendId) == null) {
                friendDao.setFriend(userId, friendId)
            }
            postService.evictFeedCacheForUser(userId)
        } catch (ex: UserNotFoundException) {
            throw UserNotFoundException("user_id not found")
        } catch (ex: CredentialException) {
            throw UserNotFoundException("user_id not found")
        }

    }

    fun deleteFriend(friendId: Int, token: String) {

        try {
            val credential = credentialService.getCredentialByToken(token)
            val friend = userService.getUser(friendId)

            friend.let {
                friendDao.deleteFriend(credential.userId!!, friendId)
            }
            postService.evictFeedCacheForUser(credential.userId!!)
        } catch (ex: UserNotFoundException) {
            throw UserNotFoundException("user_id not found")
        } catch (ex: CredentialException) {
            throw UserNotFoundException("user_id not found")
        }

    }

    fun findFriendsOf(id: Int): Set<Int> {
        return friendDao.findFriendIds(id)
    }

    fun findSubscribersOf(id: Int): Set<Int> {
        return friendDao.findSubscribersOf(id)
    }
}