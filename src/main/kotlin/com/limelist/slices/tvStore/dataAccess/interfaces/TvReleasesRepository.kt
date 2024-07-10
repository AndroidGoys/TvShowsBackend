package com.limelist.slices.tvStore.dataAccess.interfaces

import com.limelist.slices.tvStore.dataAccess.models.TvReleaseCreateModel

interface TvReleasesRepository {
    suspend fun updateMany(releases: List<TvReleaseCreateModel>)
}