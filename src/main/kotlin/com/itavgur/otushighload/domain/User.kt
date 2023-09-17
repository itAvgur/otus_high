package com.itavgur.otushighload.domain

data class User(
    var id: Int? = null,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val gender: Gender,
    val city: City? = null,
    val hobbies: List<Hobby>? = null
) : Cloneable {

    public override fun clone(): User = User(
        id = id,
        firstName = firstName,
        lastName = lastName,
        age = age,
        gender = gender,
        hobbies = hobbies?.map { it.clone() }?.toList(),
        city = city?.clone()
    )
}