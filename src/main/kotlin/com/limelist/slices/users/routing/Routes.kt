package com.limelist.slices.users.routing

import com.limelist.slices.shared.respondResult
import com.limelist.slices.users.UsersServices
import com.limelist.slices.users.services.userDataServices.UsersDataServiceInterface
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable


fun Route.getUserById(
    usersDataService: UsersDataServiceInterface
){
    get<AllUsers.User> { args ->
        val response = usersDataService.findUserById(args.id)
        return@get call.respondResult(response)
    }
}

fun Route.getMe(
    usersDataService: UsersDataServiceInterface
){
    authenticate("access-auth") {
        get<AllUsers.Me> {
            val userIdPrincipal = call.principal<UserIdPrincipal>()

            if (userIdPrincipal == null){
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }

            val response = usersDataService.getMe(
                userIdPrincipal.name.toInt()
            )

            call.respondResult(response)
        }
    }
}

@Serializable
@Resource("/users")
class AllUsers{
    @Serializable
    @Resource("/users/{id}")
    data class User(
        val id: Int
    )

    @Serializable
    @Resource("/users/@me")
    class Me()
}

fun Route.useUsers(
    root: String,
    services: UsersServices
){
    route(root){
        getUserById(services.usersDataService)
        getMe(services.usersDataService)
    }
}