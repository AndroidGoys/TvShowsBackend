package com.limelist.slices.auth.dataAccess.sqlite.repositories

import com.limelist.slices.auth.dataAccess.interfaces.AuthRepository
import com.limelist.slices.auth.services.models.*
import kotlinx.coroutines.sync.Mutex
import java.sql.Connection

class AuthSqliteRepository(
    val connection: Connection,
    val mutex: Mutex
) : AuthRepository {
    override suspend fun add(data: IdentificationData) {
        TODO("Not yet implemented")
    }

    override suspend fun update(data: IdentificationData) {
        TODO("Not yet implemented")
    }

    override suspend fun findByRefreshToken(refreshToken: RefreshTokenData): IdentificationData? {
        TODO("Not yet implemented")
    }

    override suspend fun findByLogin(login: String): IdentificationData? {
        TODO("Not yet implemented")
    }

}