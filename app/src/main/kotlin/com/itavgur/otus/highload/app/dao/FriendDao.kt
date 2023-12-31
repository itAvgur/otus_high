package com.itavgur.otus.highload.app.dao

import com.itavgur.otus.highload.app.domain.Friend

interface FriendDao {

    fun getFriend(userId: Int, friendId: Int): Friend?

    fun setFriend(userId: Int, friendId: Int): Int

    fun deleteFriend(userId: Int, friendId: Int): Int

    fun findFriendIds(id: Int): Set<Int>

    fun findSubscribersOf(id: Int): Set<Int>

}