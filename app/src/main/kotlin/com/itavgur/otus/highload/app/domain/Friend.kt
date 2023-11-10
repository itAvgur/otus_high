package com.itavgur.otus.highload.app.domain

data class Friend(
    var id: Int? = null,
    val userId: Int,
    var friendId: Int?
) : Cloneable {

    public override fun clone(): Friend = Friend(
        id = id,
        userId = userId,
        friendId = friendId
    )
}