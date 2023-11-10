package com.itavgur.otus.highload.app.dao

import com.itavgur.otus.highload.app.domain.Friend

class FriendDaoMock : FriendDao {

    private val sqlTable: MutableSet<Friend> = mutableSetOf()
    private var idSequence: Int = 1

    init {
        setFriend(1, 2)
        setFriend(2, 1)
    }

    override fun getFriend(userId: Int, friendId: Int): Friend? {
        return sqlTable.firstOrNull { it.userId == userId && it.friendId == friendId }
    }

    override fun setFriend(userId: Int, friendId: Int): Int {

        sqlTable.firstOrNull { it.userId == userId && it.friendId == friendId }
            ?: sqlTable.add(
                Friend(idSequence, userId, friendId)
            )
        ++idSequence
        return 1
    }

    override fun deleteFriend(userId: Int, friendId: Int): Int {

        val filter = sqlTable.filter { it.userId == userId && it.friendId == friendId }.toSet()

        sqlTable.removeAll(filter)
        return filter.size
    }

    override fun findFriendIds(id: Int): Set<Int> {
        return sqlTable.filter { it.userId == id }
            .map { it.friendId!! }
            .toSet()
    }

    override fun findSubscribersOf(id: Int): Set<Int> {
        return sqlTable.filter { it.friendId == id }
            .map { it.friendId!! }
            .toSet()
    }

}