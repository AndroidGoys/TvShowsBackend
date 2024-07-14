package com.limelist.slices.auth.dataAccess.sqlite.repositories

import com.limelist.slices.auth.dataAccess.interfaces.AuthRepository
import com.limelist.slices.auth.services.models.AccessTokenData
import com.limelist.slices.auth.services.models.AuthenticationTokensData
import com.limelist.slices.auth.services.models.LoginData
import com.limelist.slices.auth.services.models.RefreshTokenData
import kotlinx.coroutines.sync.Mutex
import java.sql.Connection

class AuthSqliteRepository(
    val connection: Connection,
    val mutex: Mutex
) : AuthRepository {
    override suspend fun add(userId: Int, login: String, password: String, refreshTokenId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun update(oldAuthData: LoginData, newAuthData: LoginData, newRefreshTokenId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun findTokenDataByRefreshToken(refreshToken: RefreshTokenData): AccessTokenData? {
        TODO("Not yet implemented")
    }

    override suspend fun findTokenDataByIdentificationData(login: String, password: String): AuthenticationTokensData? {
        TODO("Not yet implemented")
    }

}