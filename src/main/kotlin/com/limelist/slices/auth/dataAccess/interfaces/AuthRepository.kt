package com.limelist.slices.auth.dataAccess.interfaces

import com.limelist.slices.auth.services.models.*

interface AuthRepository {
    suspend fun add(data: IdentificationData)
    suspend fun update(data: IdentificationData)
    suspend fun findByRefreshToken(refreshToken: RefreshTokenData): IdentificationData?
    suspend fun findByLogin(login: String): IdentificationData?
}