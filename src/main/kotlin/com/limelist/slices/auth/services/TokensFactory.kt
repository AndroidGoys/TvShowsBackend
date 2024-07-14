package com.limelist.slices.auth.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.limelist.slices.auth.AuthConfig
import com.limelist.slices.auth.services.models.*
import java.time.Instant

public class TokensFactory (
    val config: AuthConfig
) {

    private val accessTokenLifetimeMS = // 20 минут * коеф
        (20 * 60 * 1000 * config.tokensLifeTimeCoefficient).toLong()
    private val refreshTokenLifetimeMS = // 20 дней * коеф
        (20 * 24 * 60 * 60 * 1000 * config.tokensLifeTimeCoefficient).toLong()

    private val accessTokenExpirationDateMS
        get() = accessTokenLifetimeMS + Instant.EPOCH.toEpochMilli()

    private val refreshTokenExpirationDateMs
        get() = accessTokenLifetimeMS + Instant.EPOCH.toEpochMilli()


    private fun createAccessTokenPrivate(
        userId: Int,
        expirationDate: Long
    ) = JWT.create()
            .withSubject(userId.toString())
            .withExpiresAt(Instant.ofEpochMilli(expirationDate))
            .sign(Algorithm.HMAC256(config.secret))
            .toString()

    private fun createRefreshTokenPrivate(
        refreshId: String,
        expirationDate: Long
    ) = JWT.create()
        .withSubject(refreshId)
        .withExpiresAt(Instant.ofEpochMilli(expirationDate))
        .sign(Algorithm.HMAC256(config.secret))
        .toString()


    fun createAccessToken(
        userId: Int
    ) = AccessToken(
        createAccessTokenPrivate(
            userId,
            accessTokenExpirationDateMS
        )
    )

    fun createRefreshToken(
        refreshId: String
    ) = RefreshToken(
        createRefreshTokenPrivate(
            refreshId,
            refreshTokenExpirationDateMs
        )
    )

    fun create(tokensData: AuthenticationTokensData): AuthenticationTokens {
        val access = createAccessTokenPrivate(
            tokensData.userId,
            accessTokenExpirationDateMS
        )
        val refresh = createRefreshTokenPrivate(
            tokensData.refreshTokenId,
            accessTokenExpirationDateMS
        )

        return  AuthenticationTokens(access, refresh)
    }
}