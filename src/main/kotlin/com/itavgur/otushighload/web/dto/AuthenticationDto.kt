package com.itavgur.otushighload.web.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(name = "UserAuthenticationRequest", description = "User authentication request")
data class AuthenticationDtoRequest(
    @Schema(name = "UserLogin", required = true)
    val login: String,
    @Schema(name = "UserPassword", required = true)
    val pass: String,
)

data class AuthenticationDtoResponse(
    @Schema(name = "BearerToken", required = true)
    val token: String,
    @Schema(name = "TokenLifetime", required = true)
    val tokenExpired: LocalDate
)