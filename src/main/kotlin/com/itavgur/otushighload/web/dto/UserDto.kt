package com.itavgur.otushighload.web.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.itavgur.otushighload.domain.City
import com.itavgur.otushighload.domain.Gender
import com.itavgur.otushighload.domain.Hobby
import com.itavgur.otushighload.domain.User
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

@Schema(name = "User", description = "User DTO")
data class UserDto(
    @Schema(name = "UserId", required = false)
    var id: Int? = null,
    @Schema(name = "UserFirstName", required = true)
    val firstName: String,
    @Schema(name = "UserSecondName", required = true)
    val lastName: String,
    @Schema(name = "UserAge", required = true)
    val age: Int,
    @Schema(name = "UserGender", required = true)
    val gender: Gender,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "City", required = false)
    val city: City? = null,
    @Schema(name = "Hobbies", required = false)
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