package com.limelist.slices.tvStore.dataAccess.interfaces

import com.limelist.slices.tvStore.dataAccess.models.create.TvTagCreateModel
import com.limelist.slices.tvStore.services.models.tags.TvTagDetails

interface TvTagsRepository : TvRepository {
    suspend fun createIfNotExists(tvTag: TvTagCreateModel): TvTagDetails
}