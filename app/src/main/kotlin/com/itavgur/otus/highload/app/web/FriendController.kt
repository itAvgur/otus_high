package com.itavgur.otus.highload.app.web

import com.itavgur.otus.highload.app.aspect.Authenticated
import com.itavgur.otus.highload.app.service.CredentialService
import com.itavgur.otus.highload.app.service.FriendService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/friend")
class FriendController(
    @Autowired val friendService: FriendService,
    @Autowired val credentialService: CredentialService
) {

    @Operation(summary = "Set friend", description = "Set new friend", tags = ["friend"])
    @PutMapping("/{friendId}")
    @Authenticated
    fun addFriend(
        @PathVariable(name = "friendId", required = true) friendId: Int,
        @RequestParam(name = "token", required = true) token: String
    ) = run {
        credentialService.getCredentialByToken(token).userId!!
        friendService.setFriend(friendId, token)
    }

    @Operation(summary = "Delete friend", description = "Delete friend by id", tags = ["friend"])
    @DeleteMapping("/{friendId}")
    @Authenticated
    fun deleteFriendById(
        @PathVariable(name = "friendId", required = true) friendId: Int,
        @RequestParam(name = "token", required = true) token: String
    ) = friendService.deleteFriend(friendId, token)

}