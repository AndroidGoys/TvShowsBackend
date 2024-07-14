package com.limelist.slices.auth.dataAccess.interfaces

import com.limelist.slices.auth.services.models.AccessTokenData
import com.limelist.slices.auth.services.models.AuthenticationTokensData
import com.limelist.slices.auth.services.models.LoginData
import com.limelist.slices.auth.services.models.RefreshTokenData

interface AuthRepository {
    suspend fun add(userId: Int, login: String, password: String, refreshTokenId: String)
    suspend fun update(oldAuthData: LoginData, newAuthData: LoginData, newRefreshTokenId: String)
    suspend fun findTokenDataByRefreshToken(refreshToken: RefreshTokenData): AccessTokenData?
    suspend fun findTokenDataByIdentificationData(login: String, password: String): AuthenticationTokensData?
}