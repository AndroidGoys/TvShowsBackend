package com.limelist.slices.shared

import java.time.Clock
import java.time.Instant

fun getCurrentUnixUtc0TimeSeconds(): Long {
    val instant = Instant.now(Clock.systemUTC())
    return instant.epochSecond
}

fun Long.normalizeUnixSecondsTime(
    timeZone: Float
) = this - Math.floor(60.0 * 60.0 * timeZone).toLong()

fun Long.changeTimeZone(
    timeZone: Float
) = this + Math.floor(60.0 * 60.0 * timeZone).toLong()
