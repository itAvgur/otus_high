package com.itavgur.otushighload.dao

import com.itavgur.otushighload.domain.Friend

class FriendDaoMock : FriendDao {

    private val sqlTable: MutableSet<Friend> = mutableSetOf()
    private var idSequence: Int = 1

    init {
        setFriend(1, 2)
        setFriend(2, 1)
    }

    override fun setFriend(userId: Int, friendId: Int) {

        sqlTable.firstOrNull { it.userId == userId && it.friendId == friendId }
            ?: sqlTable.add(
                Friend(idSequence, userId, friendId)
            )
        ++idSequence
    }

    override fun deleteFriend(userId: Int, friendId: Int): Int {

        val filter = sqlTable.filter { it.userId == userId && it.friendId == friendId }.toSet()

        sqlTable.removeAll(filter)
        return filter.size
    }

    override fun findFriends(id: Int): Set<Int> {
        return sqlTable.filter { it.userId == id }
            .map { it.friendId!! }
            .toSet()
    }

}