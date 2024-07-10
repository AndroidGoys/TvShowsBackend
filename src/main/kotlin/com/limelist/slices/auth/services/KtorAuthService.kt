package com.limelist.slices.auth.services

import com.limelist.slices.auth.AuthConfig
import com.limelist.slices.auth.services.models.AccessToken
import com.limelist.slices.auth.services.models.AuthenticationTokens
import com.limelist.slices.auth.services.models.LoginData
import com.limelist.slices.auth.services.models.RefreshToken

class KtorAuthService(
    val config: AuthConfig
) : AuthService {
    override suspend fun login(data: LoginData): Result<AuthenticationTokens> {
        TODO("Not yet implemented")
    }

    override suspend fun signup(data: LoginData): Result<AuthenticationTokens> {
        TODO("Not yet implemented")
    }

    override suspend fun refresh(refreshToken: RefreshToken): Result<AccessToken> {
        TODO("Not yet implemented")
    }

    override suspend fun updateLoginData(oldData: LoginData, newData: LoginData): Result<Unit> {
        TODO("Not yet implemented")
    }

}