package com.itavgur.otus.highload.app.domain

import com.itavgur.otus.highload.app.util.NoArgConstructor
import java.time.LocalDateTime

@NoArgConstructor
data class Message(
    var id: Int? = null,
    val text: String,
    var userFromId: Int,
    var userToId: Int,
    var created: LocalDateTime? = null
) : Cloneable {

    public override fun clone(): Message = Message(
        id = id,
        text = text,
        userFromId = userFromId,
        userToId = userToId,
        created = created,
    )
}