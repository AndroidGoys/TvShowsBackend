package com.limelist.slices.users.routing

import com.limelist.slices.shared.respondResult
import com.limelist.slices.shared.withAuth
import com.limelist.slices.users.UsersServices
import com.limelist.slices.users.services.userData.UsersDataServiceInterface
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Route.useUsers(
    root: String,
    services: UsersServices
){
    route(root){
        getUserById(services.usersDataService)
        getMe(services.usersDataService)
        useAvatars(services.avatarsSharingService)
    }
}

private fun Route.getUserById(
    usersDataService: UsersDataServiceInterface
){
    get<AllUsers.User> { args ->
        val response = usersDataService.findUserById(args.id)
        return@get call.respondResult(response)
    }
}

private fun Route.getMe(
    usersDataService: UsersDataServiceInterface
){
    authenticate("access-auth") {
        get<AllUsers.Me> {
            call.withAuth { user ->
                val response = usersDataService.getMe(
                    user.id
                )

                call.respondResult(response)
            }
        }
    }
}

@Serializable
@Resource("/users")
class AllUsers {
    @Serializable
    @Resource("/users/{id}")
    data class User(
        val id: Int
    ) {
        @Serializable
        @Resource("avatar")
        class Avatar(
            val parent: User
        )
    }
    @Serializable
    @Resource("/users/@me")
    class Me{
        @Serializable
        @Resource("/users/@me/avatar")
        class Avatar
    }
}

