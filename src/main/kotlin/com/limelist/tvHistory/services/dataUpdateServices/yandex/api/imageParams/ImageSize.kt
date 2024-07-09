package com.limelist.tvHistory.services.dataUpdateServices.yandex.api.imageParams

enum class ImageSize {
    Enormous, //— поиск картинок очень большого размера (размеры в пикселях более 1600 × 1200).
    Large, // — поиск картинок большого размера (размеры в пикселях от 800 х 600 до 1600 × 1200).
    Medium, // — поиск картинок среднего размера (размеры в пикселях от 150 × 150 до 800 × 600).
    Small, //— поиск картинок маленького размера (размеры в пикселях от 32 × 32 до 150 × 150).
    Tiny, // — поиск иконок (размеры в пикселях не более 32 × 32).
    Wallpaper; //— поиск обоев для рабочего стола.

    override fun toString() = when(this){
        Enormous -> "enormous"
        Large -> "large"
        Medium -> "medium"
        Small -> "small"
        Tiny -> "tiny"
        Wallpaper -> "wallpaper"
    }
}
