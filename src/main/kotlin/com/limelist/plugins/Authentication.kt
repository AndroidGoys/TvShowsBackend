package com.limelist.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.limelist.ApplicationConfig
import com.limelist.slices.shared.getCurrentUnixUtc0TimeSeconds
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureAuthentication(config: ApplicationConfig) {
    val algorithm = Algorithm.HMAC256(config.authConfig.secret)

    install(Authentication) {
        jwt("access-auth") {
            verifier(
                JWT.require(algorithm)
                .build()
            )

            validate { credential ->
                val subject = credential.subject

                if (subject == null)
                    return@validate null

                return@validate UserIdPrincipal(subject)
            }
        }
    }
}