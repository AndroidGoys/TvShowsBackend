package com.limelist.tvStore.services.dataUpdateServices.yandex.api.imageParams

enum class ImageOrientation {
    Horizontal,
    Vertical,
    Square;

    override fun toString() = when(this) {
        Horizontal -> "horizontal"
        Square -> "square"
        Vertical -> "vertical"
    }
}
