package com.limelist.slices.tvStore.services.dataUpdateServices.yandex.api

import io.ktor.http.ParametersBuilder

interface SearchParams {
    fun fillBuilder(builder: ParametersBuilder)
}