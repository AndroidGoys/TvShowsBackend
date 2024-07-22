package com.limelist.slices.shared

import java.time.Clock
import java.time.Instant

fun getCurrentUnixUtc0TimeSeconds(): Long {
    val instant = Instant.now(Clock.systemUTC())
    return instant.epochSecond
}
