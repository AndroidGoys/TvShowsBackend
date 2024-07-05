package com.limelist.tvHistory.dataAccess.interfaces

import com.limelist.tvHistory.dataAccess.models.TvReleaseCreateModel

interface TvReleasesRepository {
    suspend fun updateMany(releases: List<TvReleaseCreateModel>)
}