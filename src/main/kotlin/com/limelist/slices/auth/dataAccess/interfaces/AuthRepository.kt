package com.limelist.slices.auth.dataAccess.interfaces

interface AuthRepository {
    suspend fun add(userId: Int, authData: String)
    suspend fun update(oldAuthData: String, newAuthData: String)
    suspend fun getUserIdByAuthData(authData: String)
}