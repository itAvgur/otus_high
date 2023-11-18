package com.itavgur.otus.highload.app.web

import com.itavgur.otus.highload.app.aspect.Authenticated
import com.itavgur.otus.highload.app.service.CredentialService
import com.itavgur.otus.highload.app.service.MessageService
import com.itavgur.otus.highload.app.web.dto.MessageRequestDto
import com.itavgur.otus.highload.app.web.dto.MessageResponseDto
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/message")
class MessageController(
    @Autowired val messageService: MessageService,
    @Autowired val credentialService: CredentialService
) {

    @Authenticated
    @Operation(summary = "Get message", description = "Get message by id", tags = ["message"])
    @GetMapping("/{id}")
    fun getMessageById(
        @PathVariable(name = "id", required = true) id: Int,
        @RequestParam(name = "token", required = true) token: String
    ): MessageResponseDto {
        val credential = credentialService.getCredentialByToken(token)
        return messageService.getMessage(id, credential.userId!!)
    }

    @Authenticated
    @Operation(summary = "Get messages", description = "Get all messages", tags = ["message"])
    @GetMapping("/list")
    fun getMessages(
        @RequestParam(name = "token", required = true) token: String
    ): List<MessageResponseDto> {
        val credential = credentialService.getCredentialByToken(token)
        return messageService.getMessages(credential.userId!!)
    }

    @Authenticated
    @Operation(summary = "Create message", description = "Create message", tags = ["message"])
    @PostMapping
    fun createNewMessage(
        @Valid @RequestBody request: MessageRequestDto,
        @RequestParam(name = "token", required = true) token: String
    ): MessageResponseDto {
        val credential = credentialService.getCredentialByToken(token)
        return messageService.sendMessage(request, credential.userId!!)
    }

}