package com.limelist.slices.auth.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.limelist.slices.auth.AuthConfig
import com.limelist.slices.auth.services.models.*
import com.limelist.slices.shared.getCurrentUnixUtc0TimeSeconds
import java.time.Instant

public class TokensFactory (
    val config: AuthConfig
) {

    private val accessTokenLifetimeSeconds = // 20 минут * коеф
        (20 * 60 * config.tokensLifeTimeCoefficient).toLong()
    private val refreshTokenLifetimeSeconds = // 20 дней * коеф
        (20 * 24 * 60 * 60 * config.tokensLifeTimeCoefficient).toLong()

    private val accessTokenExpirationDateSeconds
        get() = accessTokenLifetimeSeconds + getCurrentUnixUtc0TimeSeconds()

    private val refreshTokenExpirationDateSeconds
        get() = accessTokenLifetimeSeconds + getCurrentUnixUtc0TimeSeconds()


    private fun createAccessTokenPrivate(
        userId: Int,
        expirationDate: Long
    ) = JWT.create()
            .withSubject(userId.toString())
            .withExpiresAt(Instant.ofEpochSecond(expirationDate))
            .sign(Algorithm.HMAC256(config.secret))
            .toString()

    private fun createRefreshTokenPrivate(
        userId: Int,
        lastUpdate: Long,
        expirationDate: Long
    ) = JWT.create()
        .withSubject(userId.toString())
        .withClaim("lastUpdate", lastUpdate)
        .withExpiresAt(Instant.ofEpochSecond(expirationDate))
        .sign(Algorithm.HMAC256(config.secret))
        .toString()


    fun createAccessToken(
        userId: Int
    ) = AccessToken(
        createAccessTokenPrivate(
            userId,
            accessTokenExpirationDateSeconds
        )
    )

    fun createRefreshToken(
        userId: Int,
        lastUpdate: Long
    ) = RefreshToken(
        createRefreshTokenPrivate(
            userId,
            lastUpdate,
            refreshTokenExpirationDateSeconds
        )
    )

    fun create(tokensData: AuthenticationTokensData): AuthenticationTokens {
        val access = createAccessTokenPrivate(
            tokensData.userId,
            accessTokenExpirationDateSeconds
        )
        val refresh = createRefreshTokenPrivate(
            tokensData.userId,
            tokensData.lastUpdate,
            refreshTokenExpirationDateSeconds
        )

        return  AuthenticationTokens(refresh, access)
    }

    fun validate(token: RefreshToken) : Result<RefreshTokenData> {
        try {
            JWT.require(Algorithm.HMAC256(config.secret))
                .build()
                .verify(token.value)
        } catch (exception: JWTVerificationException) {
            return Result.failure(exception)
        }

        val decodedToken = JWT.decode(token.value)

        val userId = decodedToken.subject.toInt()
        val expiresDate = decodedToken.expiresAtAsInstant
        val lastUpdate = decodedToken.getClaim("lastUpdate").asLong()

        return Result.success(RefreshTokenData(
            userId,
            lastUpdate,
            expiresDate.epochSecond
        ))
    }
}