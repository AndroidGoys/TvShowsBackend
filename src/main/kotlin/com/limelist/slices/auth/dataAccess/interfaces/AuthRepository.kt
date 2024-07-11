package com.limelist.slices.auth.dataAccess.interfaces

import com.limelist.slices.auth.services.models.AccessTokenData

interface AuthRepository {
    suspend fun add(userId: Int, authData: String, refreshTokenId: String)
    suspend fun update(oldAuthData: String, newAuthData: String, refreshTokenId: String)
    suspend fun getTokenDataByRefreshToken(refreshTokenId: String): AccessTokenData
}