package com.limelist.slices.users.routing

import com.limelist.slices.shared.respondWithResult
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
    get<Users.User> { args ->
        val response = usersDataService.findUserById(args.id)
        return@get call.respondWithResult(response)
    }
}

fun Route.getMe(
    usersDataService: UsersDataServiceInterface
){
    authenticate {
        get<Users.Me> {
            val userIdPrincipal = call.principal<UserIdPrincipal>()

            if (userIdPrincipal == null){
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }

            val response = usersDataService.getMe(
                userIdPrincipal.name.toInt()
            )

            call.respondWithResult(response)
        }
    }
}

@Serializable
@Resource("/users")
class Users{
    @Resource("/{id}")
    data class User(
        val id: Int
    )

    @Resource("/@me")
    class Me()
}

fun Route.useUsers(services: UsersServices){
    route("/users"){
        getUserById(services.usersDataService)
        getMe(services.usersDataService)
    }
}