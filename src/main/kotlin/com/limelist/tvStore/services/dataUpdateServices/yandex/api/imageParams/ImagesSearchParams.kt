package com.limelist.tvStore.services.dataUpdateServices.yandex.api.imageParams

import com.limelist.tvStore.services.dataUpdateServices.yandex.api.SearchParams
import io.ktor.http.ParametersBuilder


data class ImagesSearchParams(
    val query: String,
    val groupBy: UInt = 20.toUInt(), //Количество результатов на странице
    val page: UInt = 1.toUInt(), //Номер страницы
    val useFamilyFilter: Boolean = true,
    val site: String? = null,
    val imageType: ImageType? = null,
    val imageOrientation: ImageOrientation? = null,
    val imageSize: ImageSize? = null,
    val imageColor: ImageColor? = null
)  : SearchParams {
    override fun fillBuilder(builder: ParametersBuilder) {
        val fyandex = if (useFamilyFilter) 1 else 0

        builder.append("text", query)
        builder.append("groupby", "attr=ii.groups-on-page=$groupBy")
        builder.append("p", page.toString())
        builder.append("fyandex", fyandex.toString())

        site?.let {
            builder.append("site", site.toString())
        }
        imageType?.let {
            builder.append("itype", imageType.toString())
        }
        imageOrientation?.let {
            builder.append("iorient", imageOrientation.toString())
        }
        imageSize?.let {
            builder.append("isize", imageSize.toString())
        }
        imageColor?.let {
            builder.append("icolor", imageColor.toString())
        }
    }
}