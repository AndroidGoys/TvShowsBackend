package com.limelist.slices.tvStore.dataAccess.interfaces

import com.limelist.slices.tvStore.dataAccess.models.create.TvReleaseCreateModel

interface TvReleasesRepository : TvRepository {
    suspend fun updateMany(releases: List<TvReleaseCreateModel>)
}