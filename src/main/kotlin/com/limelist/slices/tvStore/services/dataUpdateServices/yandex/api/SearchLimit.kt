package com.limelist.slices.tvStore.services.dataUpdateServices.yandex.api

import java.time.OffsetDateTime

data class SearchLimit(
    val from: OffsetDateTime,
    val to: OffsetDateTime,
    val limit: Int,
){
    val rps get() = limit / 3420
}
