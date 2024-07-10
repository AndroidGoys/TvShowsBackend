package com.limelist.slices.tvStore.services.dataUpdateServices.yandex.api.limitParams

import com.limelist.slices.tvStore.services.dataUpdateServices.yandex.api.SearchParams
import io.ktor.http.*

class limitSearchParams(
    val action: String = "limits-info"
): SearchParams {
    override fun fillBuilder(builder: ParametersBuilder) {
        builder.append("action", action)
    }
}