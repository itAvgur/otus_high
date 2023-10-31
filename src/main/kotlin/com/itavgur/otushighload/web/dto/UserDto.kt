package com.itavgur.otushighload.web.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.itavgur.otushighload.domain.City
import com.itavgur.otushighload.domain.Gender
import com.itavgur.otushighload.domain.Hobby
import com.itavgur.otushighload.domain.User
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import java.io.Serializable

@Schema(name = "User", description = "User DTO")
data class UserDto(
    @Schema(name = "id", required = false)
    var id: Int? = null,
    @Schema(name = "firstName", required = true)
    val firstName: String,
    @Schema(name = "lastName", required = true)
    val lastName: String,
    @Schema(name = "age", required = true)
    val age: Int,
    @Schema(name = "gender", required = true)
    val gender: Gender,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "city", required = false)
    val city: City? = null,
    @Schema(name = "hobbies", required = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val hobbies: List<Hobby>? = null
) : Serializable {

    fun toUser(): User {
        return User(
            id = id,
            firstName = firstName, lastName = lastName,
            age = age, gender = gender, city = city,
            hobbies = hobbies
        )
    }

    companion object {
        fun from(user: User): UserDto {
            return UserDto(
                id = user.id,
                firstName = user.firstName, lastName = user.lastName,
                age = user.age, gender = user.gender, city = user.city,
                hobbies = user.hobbies
            )
        }
    }

}

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