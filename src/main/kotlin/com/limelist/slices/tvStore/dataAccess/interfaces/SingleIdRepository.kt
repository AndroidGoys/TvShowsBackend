package com.limelist.slices.tvStore.dataAccess.interfaces

interface SingleIdRepository<TId> : TvRepository {
    suspend fun contains(id: TId): Boolean
}
