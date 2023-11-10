package com.itavgur.otus.highload.app.dao

import com.itavgur.otus.highload.app.domain.Message

interface MessageDao {

    fun getMessage(id: Int, userId: Int): Message?

    fun getMessages(userId: Int): List<Message>

    fun sendMessage(message: Message): Message

}