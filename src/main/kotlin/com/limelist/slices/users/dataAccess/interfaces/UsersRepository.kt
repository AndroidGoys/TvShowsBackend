package com.limelist.slices.users.dataAccess.interfaces

import com.limelist.slices.users.services.models.UserData

interface UsersRepository {
    suspend fun add(data: UserData) : UserData
    suspend fun findByEmail(email: String): UserData?
}