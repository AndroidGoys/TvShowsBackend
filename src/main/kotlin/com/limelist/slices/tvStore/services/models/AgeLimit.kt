package com.limelist.slices.tvStore.services.models

import com.limelist.slices.tvStore.services.EnumFlag
import com.limelist.slices.tvStore.services.models.serialization.AgeLimitSerializer
import kotlinx.serialization.Serializable

@Serializable(with = AgeLimitSerializer::class)
class AgeLimit(
    flag: Int
): EnumFlag(flag) {
    companion object {
        val NoRestrictions = AgeLimit(0b0000) // 0+
        val OverSixYearsOld = AgeLimit(0b0001) // 6+
        val OverTwelveYearsOld = AgeLimit(0b0010) // 12+
        val OverSixteenYearsOld = AgeLimit(0b0011) // 16+
        val OverEighteenYearsOld = AgeLimit(0b0100) // 18+

        val GeneralAudiences = AgeLimit(0b1000) // 0+ G
        val ParentalGuidanceSuggested = AgeLimit(0b1001) // PG Детям рекомендуется смотреть фильм с родителями
        val ParentsStronglyCautioned = AgeLimit(0b1010) // PG-13 Просмотр не желателен детям до 13 лет.
        val Restricted = AgeLimit(0b1011)  // R Лица, не достигшие 17-летнего возраста, допускаются на фильм только в сопровождении одного из родителей
        val NoOne17AndUnderAdmitted = AgeLimit(0b1100) // NC-17 Лица 17-летнего возраста и младше на фильм не допускаются;

        fun fromInt(ageLimitValue: Int): AgeLimit {
            return AgeLimit(ageLimitValue)
        }
    }
}