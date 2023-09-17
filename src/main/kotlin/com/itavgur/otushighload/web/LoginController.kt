package com.itavgur.otushighload.web

import com.itavgur.otushighload.service.CredentialService
import com.itavgur.otushighload.web.dto.AuthenticationDtoRequest
import com.itavgur.otushighload.web.dto.AuthenticationDtoResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/login")
class LoginController(
    @Autowired val credentialService: CredentialService
) {

    @Operation(summary = "Authenticate user", description = "Authenticate user by login/password, get bearer token")
    @PostMapping
    fun authenticate(@Valid @RequestBody authenticationDto: AuthenticationDtoRequest): AuthenticationDtoResponse =
        credentialService.authenticate(authenticationDto)

}