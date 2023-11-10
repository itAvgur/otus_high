package com.itavgur.otus.highload.app.dao

import com.itavgur.otus.highload.app.config.PostgresDBConfig
import com.itavgur.otus.highload.app.domain.Friend
import com.itavgur.otus.highload.app.util.DBUtil.Companion.getIntValue
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
@ConditionalOnBean(PostgresDBConfig::class)
class FriendDaoPostgres(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : FriendDao {
    companion object {

        const val QUERY_GET_FRIEND_BY_USER_AND_FRIEND_IDS =
            """SELECT id ,user_id, friend_id FROM friends WHERE friend_id=:friend_id"""

        const val QUERY_GET_ALL_FRIENDS_BY_USER =
            """SELECT id ,user_id, friend_id FROM friends WHERE user_id=:user_id"""

        const val QUERY_GET_ALL_SUBSCRIBERS_OF_USER =
            """SELECT id ,user_id, friend_id FROM friends WHERE friend_id=:user_id"""

        const val QUERY_INSERT_FRIEND =
            """INSERT INTO friends (user_id, friend_id) VALUES (:user_id, :friend_id)"""

        const val QUERY_DELETE_FRIEND =
            """DELETE FROM friends WHERE user_id = :user_id AND friend_id = :friend_id"""

    }

    class FriendRowMapper : RowMapper<Friend> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Friend {

            return Friend(
                id = getIntValue("id", rs),
                userId = getIntValue("user_id", rs)!!,
                friendId = getIntValue("friend_id", rs),
            )
        }
    }

    override fun getFriend(userId: Int, friendId: Int): Friend? {
        return namedParameterJdbcTemplate.query(
            QUERY_GET_FRIEND_BY_USER_AND_FRIEND_IDS,
            mapOf("user_id" to userId, "friend_id" to friendId), FriendRowMapper()
        ).ifEmpty { null }?.get(0)
    }

    override fun setFriend(userId: Int, friendId: Int): Int {
        return namedParameterJdbcTemplate.update(
            QUERY_INSERT_FRIEND,
            mapOf("user_id" to userId, "friend_id" to friendId)
        )
    }

    override fun deleteFriend(userId: Int, friendId: Int): Int {
        return namedParameterJdbcTemplate.update(
            QUERY_DELETE_FRIEND,
            mapOf("user_id" to userId, "friend_id" to friendId)
        )
    }

    override fun findFriendIds(id: Int): Set<Int> {

        return namedParameterJdbcTemplate.query(
            QUERY_GET_ALL_FRIENDS_BY_USER,
            mapOf("user_id" to id), FriendRowMapper()
        ).map { it.friendId!! }.toSet()
    }

    override fun findSubscribersOf(id: Int): Set<Int> {
        return namedParameterJdbcTemplate.query(
            QUERY_GET_ALL_SUBSCRIBERS_OF_USER,
            mapOf("user_id" to id), FriendRowMapper()
        ).map { it.userId }.toSet()
    }
}