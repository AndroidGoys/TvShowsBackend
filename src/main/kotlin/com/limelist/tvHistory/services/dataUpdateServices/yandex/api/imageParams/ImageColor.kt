package com.limelist.tvHistory.services.dataUpdateServices.yandex.api.imageParams

enum class ImageColor {
    Gray, // — черно-белые;
    Color, // — цветные;
    Red, // — красные;
    Orange, // — оранжевые;
    Yellow, // — желтые;
    Green, // — зеленые;
    Cyan, // — голубые;
    Blue, // — синие;
    Violet, // — фиолетовые;
    White, // — белые;
    Black; //— черные.

    override fun toString() = when(this) {
        Gray -> "gray"
        Color -> "color"
        Red -> "red"
        Orange -> "orange"
        Yellow -> "yellow"
        Green -> "green"
        Cyan -> "cyan"
        Blue -> "blue"
        Violet -> "violet"
        White -> "white"
        Black -> "black"
    }
}
