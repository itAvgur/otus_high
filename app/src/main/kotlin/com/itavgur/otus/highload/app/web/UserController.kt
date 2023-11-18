package com.itavgur.otus.highload.app.web

import com.itavgur.otus.highload.app.aspect.Authenticated
import com.itavgur.otus.highload.app.service.UserService
import com.itavgur.otus.highload.app.web.dto.UserDto
import com.itavgur.otus.highload.app.web.dto.UserRegistrationRequest
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

    @Operation(summary = "Register user", description = "Register user", tags = ["user"])
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: UserRegistrationRequest): UserDto =
        userService.registerUser(request)

    @Operation(summary = "Get user by id", description = "Get user by id", tags = ["user"])
    @GetMapping("/get/{id}")
    @Authenticated
    fun getById(
        @PathVariable(name = "id", required = true) id: Int,
        @RequestParam(name = "token", required = false) token: String? = null
    ): UserDto = userService.getUser(id)

    @Operation(summary = "Get all users", description = "Get all users", tags = ["user"])
    @GetMapping("/get/all")
    fun getAll(): List<UserDto> = userService.getUsers()

    @Operation(
        summary = "Search users by first/last name",
        description = "Search users by first and last name",
        tags = ["user"]
    )
    @GetMapping("/search")
    fun searchUsers(
        @RequestParam(name = "firstName", required = false) firstName: String?,
        @RequestParam(name = "lastName", required = false) lastName: String?
    ): List<UserDto> = userService.searchUsers(firstName, lastName)

}