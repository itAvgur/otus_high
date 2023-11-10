package com.itavgur.otus.highload.app.dao

import com.itavgur.otus.highload.app.domain.Message
import java.time.LocalDateTime

class MessageDaoMock : MessageDao {

    private val sqlTable: MutableSet<Message> = mutableSetOf()
    private var idSequence: Int = 1
    override fun getMessage(id: Int, userId: Int): Message? {
        return sqlTable.firstOrNull { it.id == id }
    }

    override fun getMessages(userId: Int): List<Message> {
        return sqlTable.filter { it.userFromId == userId || it.userToId == userId }
    }

    override fun sendMessage(message: Message): Message {
        message.id = idSequence
        message.created = LocalDateTime.now()
        sqlTable.add(message)
        ++idSequence
        return message.clone()
    }
}