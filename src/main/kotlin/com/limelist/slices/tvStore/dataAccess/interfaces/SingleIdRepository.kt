package com.limelist.slices.tvStore.dataAccess.interfaces

interface SngleIdRepository<TId> {
    suspend fun contains(id: TId): Boolean
}
