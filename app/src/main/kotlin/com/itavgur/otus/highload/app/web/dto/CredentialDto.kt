package com.itavgur.otus.highload.app.web.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "UserCredentialRequest", description = "User credential request")
data class CredentialDto(
    @Schema(name = "login", required = true)
    var login: String,
    @Schema(name = "pass", required = true)
    var pass: String,
    @Schema(name = "passRepeat", required = true)
    var passRepeat: String,
    @Schema(name = "user", description = "Users info", required = true)
    var user: UserDto
)