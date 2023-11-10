package com.itavgur.otus.highload.app.web.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(name = "UserAuthenticationRequest", description = "User authentication request")
data class AuthenticationDtoRequest(
    @Schema(name = "login", required = true)
    val login: String,
    @Schema(name = "pass", required = true)
    val pass: String,
)

data class AuthenticationDtoResponse(
    @Schema(name = "token", description = "Bearer token", required = true)
    val token: String,
    @Schema(name = "tokenExpired", description = "Expired data", required = true)
    val tokenExpired: LocalDate
)