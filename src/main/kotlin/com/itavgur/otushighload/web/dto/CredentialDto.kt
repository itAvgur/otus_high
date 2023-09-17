package com.itavgur.otushighload.web.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "UserCredentialRequest", description = "User credential request")
data class CredentialDto(
    @Schema(name = "UserLogin", required = true)
    var login: String,
    @Schema(name = "UserPassword", required = true)
    var pass: String,
    @Schema(name = "UserPassword", required = true)
    var passRepeat: String,
    @Schema(name = "User", required = true)
    var user: UserDto
)