package com.itavgur.otushighload.dao

interface FriendDao {

    fun setFriend(userId: Int, friendId: Int)
    fun deleteFriend(userId: Int, friendId: Int): Int
    fun findFriends(id: Int): Set<Int>

}