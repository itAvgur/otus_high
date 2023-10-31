package com.itavgur.otushighload.service

import com.itavgur.otushighload.dao.FriendDao
import com.itavgur.otushighload.exception.CredentialException
import com.itavgur.otushighload.exception.UserNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FriendService(
    @Autowired private val friendDao: FriendDao,
    @Autowired private val userService: UserService,
    @Autowired private val credentialService: CredentialService
) {
    fun setFriend(friendId: Int, token: String) {

        try {
            var credential = credentialService.getCredentialByToken(token)
            userService.getUser(friendId)

            friendDao.setFriend(credential.userId!!, friendId)

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
        } catch (ex: UserNotFoundException) {
            throw UserNotFoundException("user_id not found")
        } catch (ex: CredentialException) {
            throw UserNotFoundException("user_id not found")
        }

    }

    fun findFriendsOf(id: Int): Set<Int> {
        return friendDao.findFriends(id)
    }
}