package com.limelist.tvHistory.dataAccess.interfaces

import com.limelist.tvHistory.dataAccess.models.TvReleaseDataModel

interface TvReleasesRepository {
    suspend fun updateMany(releases: List<TvReleaseDataModel>)
}