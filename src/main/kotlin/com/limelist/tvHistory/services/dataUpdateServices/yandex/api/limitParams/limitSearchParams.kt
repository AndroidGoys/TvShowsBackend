package com.limelist.tvHistory.services.dataUpdateServices.yandex.api.limitParams

import com.limelist.tvHistory.services.dataUpdateServices.yandex.api.SearchParams
import io.ktor.http.*
import java.sql.Struct

class limitSearchParams(
    val action: String = "limits-info"
): SearchParams {
    override fun fillBuilder(builder: ParametersBuilder) {
        builder.append("action", action)
    }
}