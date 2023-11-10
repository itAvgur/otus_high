package com.itavgur.otushighload.dao

import com.itavgur.otushighload.domain.Message

interface MessageDao {

    fun getMessage(id: Int, userId: Int): Message?

    fun getMessages(userId: Int): List<Message>

    fun sendMessage(message: Message): Message

}