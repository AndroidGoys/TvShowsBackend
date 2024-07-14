package com.limelist.slices.auth.dataAccess.interfaces

import com.limelist.slices.auth.services.models.*

interface AuthRepository {
    suspend fun add(data: IdentificationData): Int
    suspend fun update(data: IdentificationData): Int
    suspend fun findByUserId(userId: Int): IdentificationData?
    suspend fun findByLogin(login: String): IdentificationData?
}