package com.itavgur.otus.highload.app.web.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.itavgur.otus.highload.app.domain.Message
import com.itavgur.otus.highload.app.util.NoArgConstructor
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.io.Serializable

@Schema(name = "MessageSend", description = "Message to send from user to user")
@NoArgConstructor
data class MessageRequestDto(
    @Schema(name = "text", required = true) @field:NotBlank val text: String,
    @Schema(name = "to", required = true) @JsonProperty("to") @field:NotNull var userToLogin: String
) : Serializable

@Schema(name = "MessageResponse", description = "Message from user to user response")
@NoArgConstructor
data class MessageResponseDto(
    @Schema(name = "id", required = false) var id: Int,
    @Schema(name = "text", required = true) val text: String,
    @Schema(name = "from", required = false) @JsonProperty("from") var userFromId: Int,
    @Schema(name = "to", required = true) @JsonProperty("to") @field:NotNull var userToId: Int

) : Serializable {

    companion object {
        fun from(message: Message): MessageResponseDto {
            return MessageResponseDto(
                id = message.id!!, text = message.text, userFromId = message.userFromId, userToId = message.userToId
            )
        }
    }

}