package com.limelist.tvHistory.services.dataUpdateServices.yandex.api.imageParams

enum class ImageType {
    JPG,
    GIF,
    PNG;

    override fun toString() = when (this) {
        JPG -> "jpg"
        GIF -> "gif"
        PNG -> "png"
    }
}
