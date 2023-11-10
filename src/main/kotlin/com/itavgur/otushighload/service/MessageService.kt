package com.itavgur.otushighload.service

import com.itavgur.otushighload.dao.MessageDao
import com.itavgur.otushighload.domain.Message
import com.itavgur.otushighload.exception.MessageNotFoundException
import com.itavgur.otushighload.web.dto.MessageRequestDto
import com.itavgur.otushighload.web.dto.MessageResponseDto
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