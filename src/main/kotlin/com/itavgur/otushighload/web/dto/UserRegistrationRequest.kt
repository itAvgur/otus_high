package com.itavgur.otushighload.web.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(name = "UserRegistrationRequest", description = "User registration request")
data class UserRegistrationRequest(
    @Schema(name = "UserLogin", required = true)
    var login: String,
    @Schema(name = "UserPassword", required = true)
    @field:NotBlank
    var pass: String,
    @Schema(name = "UserPassword", required = true)
    @field:NotBlank
    var passRepeat: String,
    @Schema(name = "User", required = true)
    var user: UserDto
)