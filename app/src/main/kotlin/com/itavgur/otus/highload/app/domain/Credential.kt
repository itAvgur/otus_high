package com.itavgur.otus.highload.app.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDate

class Credential(
    @JsonIgnore
    var id: Int? = null,
    @JsonIgnore
    var userId: Int? = null,
    val login: String,
    var pass: String,
    @JsonIgnore
    var token: String? = null,
    @JsonIgnore
    var tokenExpired: LocalDate? = null,
    var enabled: Boolean
) : Cloneable {

    public override fun clone(): Credential = Credential(
        id = id,
        userId = userId,
        login = login,
        pass = pass,
        enabled = enabled
    )
}