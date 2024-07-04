package com.limelist.shared

fun Long.normalizeUnixSecondsTime(
    timeZone: Float
) = this - Math.floor(60.0 * 60.0 * timeZone).toLong()