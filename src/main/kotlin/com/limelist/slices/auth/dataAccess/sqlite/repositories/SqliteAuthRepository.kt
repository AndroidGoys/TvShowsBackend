package com.limelist.slices.auth.dataAccess.sqlite.repositories

import com.limelist.slices.auth.dataAccess.interfaces.AuthRepository
import com.limelist.slices.auth.services.models.AccessTokenData

class SqliteAuthRepository : AuthRepository {
    override suspend fun add(userId: Int, authData: String, refreshTokenId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun update(oldAuthData: String, newAuthData: String, refreshTokenId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getTokenDataByRefreshToken(
        refreshTokenId: String
    ) : AccessTokenData {
        TODO("Not yet implemented")
    }
}