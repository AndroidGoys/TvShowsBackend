package com.limelist.slices.auth.services

import com.limelist.slices.auth.AuthConfig
import com.limelist.slices.auth.services.models.*

public class TokensFactory (
    val config: AuthConfig
) {
    fun createAccessToken(accessTokenData: AccessTokenData): AccessToken {
        TODO()
    }

    fun createRefreshToken(refreshTokenData: RefreshTokenData): RefreshToken {
        TODO()
    }

    fun create(tokensData: AuthenticationTokensData): AuthenticationTokens {
        TODO()
    }
}