package com.limelist.slices.tvStore.dataAccess.interfaces

interface TvRepository {
    suspend fun count(): Int
}