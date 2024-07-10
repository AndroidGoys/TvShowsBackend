package com.limelist.tvStore.dataAccess.interfaces

interface TvRepository {
    suspend fun count(): Int
}