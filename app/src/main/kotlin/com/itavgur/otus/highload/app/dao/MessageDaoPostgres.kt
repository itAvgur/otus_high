package com.itavgur.otus.highload.app.dao

import com.itavgur.otus.highload.app.config.PostgresDBConfig
import com.itavgur.otus.highload.app.domain.Message
import com.itavgur.otus.highload.app.util.DBUtil
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDateTime

@Repository
@ConditionalOnBean(PostgresDBConfig::class)
class MessageDaoPostgres(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : MessageDao {

    companion object {

        const val QUERY_GET_MESSAGE_BY_ID = """SELECT id ,user_from_id, user_to_id, text, created
                FROM messages WHERE id=:messageId AND (user_from_id = :userId OR user_to_id = :userId)"""

        const val QUERY_GET_USERS_MESSAGES = """SELECT id ,user_from_id, user_to_id, text, created
                FROM messages WHERE user_from_id = :userId OR user_to_id = :userId"""

        const val QUERY_INSERT_MESSAGE =
            """INSERT INTO messages (user_from_id, user_to_id, text, created 
                ) VALUES (:userFromId, :userToId, :text, :created)"""

    }

    class MessageRowMapper : RowMapper<Message> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Message {

            return Message(
                id = DBUtil.getIntValue("id", rs),
                text = DBUtil.getStringValue("text", rs)!!,
                userFromId = DBUtil.getIntValue("user_from_id", rs)!!,
                userToId = DBUtil.getIntValue("user_to_id", rs)!!,
                created = DBUtil.getDateTimeValue("created", rs)
            )
        }
    }

    override fun getMessage(id: Int, userId: Int): Message? {
        return namedParameterJdbcTemplate.query(
            QUERY_GET_MESSAGE_BY_ID, mapOf("messageId" to id, "userId" to userId), MessageRowMapper()
        ).ifEmpty { null }?.get(0)
    }

    override fun getMessages(userId: Int): List<Message> {
        return namedParameterJdbcTemplate.query(
            QUERY_GET_USERS_MESSAGES, mapOf("userId" to userId), MessageRowMapper()
        )
    }

    override fun sendMessage(message: Message): Message {
        message.created = LocalDateTime.now()

        val map = MapSqlParameterSource(
            mapOf(
                "text" to message.text, "userFromId" to message.userFromId, "userToId" to message.userToId,
                "created" to message.created
            )
        )

        val generatedKeyHolder = GeneratedKeyHolder()
        namedParameterJdbcTemplate.update(QUERY_INSERT_MESSAGE, map, generatedKeyHolder)

        message.id = generatedKeyHolder.keyList.first()["id"] as Int?
        return message.clone()
    }
}