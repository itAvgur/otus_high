package com.itavgur.otus.highload.app.service

import com.itavgur.otus.highload.app.dao.MessageDao
import com.itavgur.otus.highload.app.domain.Message
import com.itavgur.otus.highload.app.exception.MessageNotFoundException
import com.itavgur.otus.highload.app.web.dto.MessageRequestDto
import com.itavgur.otus.highload.app.web.dto.MessageResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MessageService(
    @Autowired private val messageDao: MessageDao,
    @Autowired private val credentialService: CredentialService
) {

    fun getMessage(id: Int, userId: Int): MessageResponseDto {
        messageDao.getMessage(id, userId)?.let {
            return MessageResponseDto.from(it)
        }
        throw MessageNotFoundException("message with id $id missed")
    }

    fun getMessages(userId: Int): List<MessageResponseDto> {
        return messageDao.getMessages(userId).map { MessageResponseDto.from(it) }
    }

    fun sendMessage(request: MessageRequestDto, userId: Int): MessageResponseDto {

        val user = credentialService.getCredentialByLogin(request.userToLogin)

        val message = Message(text = request.text, userFromId = userId, userToId = user.userId!!)
        val messageSaved = messageDao.sendMessage(message)
        return MessageResponseDto.from(messageSaved)
    }

}