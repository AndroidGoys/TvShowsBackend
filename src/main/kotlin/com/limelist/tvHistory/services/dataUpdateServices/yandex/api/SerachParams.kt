package com.limelist.tvHistory.services.dataUpdateServices.yandex.api

import io.ktor.http.ParametersBuilder

interface SearchParams {
    fun fillBuilder(builder: ParametersBuilder)
}