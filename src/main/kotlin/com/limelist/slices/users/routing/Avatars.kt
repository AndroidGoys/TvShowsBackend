package com.limelist.slices.users.routing

import com.limelist.slices.shared.RequestResult
import com.limelist.slices.shared.respondResult
import com.limelist.slices.shared.withAuth
import com.limelist.slices.users.services.avatars.AvatarsSharingService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.response.*

fun Route.useAvatars(avatars: AvatarsSharingService) {
    getAvatar(avatars)
    authenticate("access-auth") {
        getMyAvatar(avatars)
        setAvatar(avatars)
    }
}

private suspend fun ApplicationCall.getAvatarPrivate(
    avatars: AvatarsSharingService,
    userId: Int
){
    val result = avatars.getAvatarFile(userId)
    if (result is RequestResult.FailureResult){
        respondResult<Unit>(result)
        return
    }

    val file = result.unwrap()

    response.headers.append("Content-Type", "image/${file.extension}")
    respondFile(file)
}

private fun Route.getAvatar(avatars: AvatarsSharingService) {
    get<AllUsers.User.Avatar> { args ->
        call.getAvatarPrivate(avatars, args.parent.id)
    }
}

private fun Route.getMyAvatar(avatars: AvatarsSharingService){
    get<AllUsers.Me.Avatar>{
        call.withAuth { data ->
            call.getAvatarPrivate(avatars, data.id)
        }
    }
}

fun Route.setAvatar(avatars: AvatarsSharingService) {
    post<AllUsers.Me.Avatar> {
        call.withAuth { data ->
            val stream = call.receiveStream()
            val contentType = call.request.headers["Content-Type"]
            val result = avatars.loadAvatar(
                data.id,
                contentType,
                stream
            )

            call.respondResult(result)
        }
    }
}



