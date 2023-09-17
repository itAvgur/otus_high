package com.itavgur.otushighload.web

import com.itavgur.otushighload.aspect.Authenticated
import com.itavgur.otushighload.service.UserService
import com.itavgur.otushighload.web.dto.UserDto
import com.itavgur.otushighload.web.dto.UserRegistrationRequest
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    @Autowired
    val userService: UserService
) {

    @Operation(summary = "Register user", description = "Register user")
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: UserRegistrationRequest): UserDto =
        userService.registerUser(request)

    @Operation(summary = "Get user by id", description = "Get user by id")
    @GetMapping("/get/{id}")
    @Authenticated
    fun getById(
        @PathVariable(name = "id", required = true) id: Int,
        @RequestParam(name = "token", required = false) token: String? = null
    ): UserDto = userService.getUser(id)

    @Operation(summary = "Get all users", description = "Get all users")
    @GetMapping("/get/all")
    fun getAll(): List<UserDto> = userService.getUsers()

}