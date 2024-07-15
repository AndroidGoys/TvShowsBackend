package com.limelist.slices.auth.services

import com.limelist.slices.auth.services.models.*
import com.limelist.slices.users.services.models.RegistrationData
import com.limelist.slices.shared.RequestResult

interface TokenIssuanceService {
    suspend fun login(data: LoginData): RequestResult<AuthenticationTokens>
    suspend fun refresh(refreshToken: RefreshTokenData): RequestResult<AccessToken>
    suspend fun register(data: RegistrationData): RequestResult<AuthenticationTokens>
    suspend fun validate(token: RefreshToken): RequestResult<RefreshTokenData>
}