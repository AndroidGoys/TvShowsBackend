package com.limelist.slices.auth.services

import com.limelist.slices.auth.services.models.AccessToken
import com.limelist.slices.auth.services.models.AuthenticationTokens
import com.limelist.slices.auth.services.models.LoginData
import com.limelist.slices.auth.services.models.RefreshToken

interface AuthService {
    suspend fun login(data: LoginData): Result<AuthenticationTokens>
    suspend fun signup(data: LoginData): Result<AuthenticationTokens>
    suspend fun refresh(refreshToken: RefreshToken): Result<AccessToken>
    suspend fun updateLoginData(oldData:LoginData, newData:LoginData): Result<Unit>
}