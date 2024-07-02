package com.limelist.tvHistory.models

import kotlinx.serialization.Serializable

@Serializable
enum class AgeLimit(val flag: Int) {
    NoRestrictions(0b0000), // 0+
    OverSixYearsOld(0b0001), // 6+
    OverTwelveYearsOld(0b0010), // 12+
    OverSixteenYearsOld(0b0011), // 16+
    OverEighteenYearsOld(0b0100), // 18+

    GeneralAudiences(0b1000), // 0+ G
    ParentalGuidanceSuggested(0b1001), // PG Детям рекомендуется смотреть фильм с родителями
    ParentsStronglyCautioned(0b1010), // PG-13 Просмотр не желателен детям до 13 лет.
    Restricted(0b1011),  // R Лица, не достигшие 17-летнего возраста, допускаются на фильм только в сопровождении одного из родителей
    NoOne17AndUnderAdmitted(0b1100); // NC-17 Лица 17-летнего возраста и младше на фильм не допускаются;

    companion object {
        fun fromInt(ageLimitValue: Int): AgeLimit {
            return values().firstOrNull { it.flag == ageLimitValue }?: throw IllegalArgumentException("Invalid age limit value")
        }
    }
}