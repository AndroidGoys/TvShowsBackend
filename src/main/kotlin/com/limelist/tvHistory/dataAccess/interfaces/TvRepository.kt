package com.limelist.tvHistory.dataAccess.interfaces

interface TvRepository {
    suspend fun count(): Int
}