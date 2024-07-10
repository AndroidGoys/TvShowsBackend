package com.limelist.tvStore.dataAccess.interfaces

import com.limelist.tvStore.dataAccess.models.TvReleaseCreateModel

interface TvReleasesRepository {
    suspend fun updateMany(releases: List<TvReleaseCreateModel>)
}