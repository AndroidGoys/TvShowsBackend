package com.limelist.slices.auth.dataAccess.sqlite.repositories

import com.limelist.slices.auth.dataAccess.interfaces.AuthRepository

class SqliteAuthRepository : AuthRepository {
    override suspend fun add(userId: Int, authData: String) {
        TODO("Not yet implemented")
    }

    override suspend fun update(oldAuthData: String, newAuthData: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserIdByAuthData(authData: String) {
        TODO("Not yet implemented")
    }
}