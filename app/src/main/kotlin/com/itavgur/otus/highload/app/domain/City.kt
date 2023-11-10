package com.itavgur.otus.highload.app.domain

import com.fasterxml.jackson.annotation.JsonIgnore

data class City(
    @JsonIgnore
    var id: Int,
    val name: String
) : Cloneable {
    public override fun clone(): City = City(id, name)
}