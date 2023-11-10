package com.itavgur.otus.highload.app.domain

data class Hobby(
    val id: Int,
    val name: String,
    val description: String? = null
) : Cloneable {
    public override fun clone(): Hobby = Hobby(id = id, name = name, description = description)
}