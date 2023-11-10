package com.itavgur.otushighload.web

import com.itavgur.otushighload.aspect.Authenticated
import com.itavgur.otushighload.service.CredentialService
import com.itavgur.otushighload.service.MessageService
import com.itavgur.otushighload.web.dto.MessageRequestDto
import com.itavgur.otushighload.web.dto.MessageResponseDto
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
    @Operation(summary = "Get message", description = "Get message by id")
    @GetMapping("/{id}")
    fun getMessageById(
        @PathVariable(name = "id", required = true) id: Int,
        @RequestParam(name = "token", required = true) token: String
    ): MessageResponseDto {
        val credential = credentialService.getCredentialByToken(token)
        return messageService.getMessage(id, credential.userId!!)
    }

    @Authenticated
    @Operation(summary = "Get messages", description = "Get all messages")
    @GetMapping("/list")
    fun getMessages(
        @RequestParam(name = "token", required = true) token: String
    ): List<MessageResponseDto> {
        val credential = credentialService.getCredentialByToken(token)
        return messageService.getMessages(credential.userId!!)
    }

    @Authenticated
    @Operation(summary = "Create message", description = "Create message")
    @PostMapping
    fun createNewMessage(
        @Valid @RequestBody request: MessageRequestDto,
        @RequestParam(name = "token", required = true) token: String
    ): MessageResponseDto {
        val credential = credentialService.getCredentialByToken(token)
        return messageService.sendMessage(request, credential.userId!!)
    }

}