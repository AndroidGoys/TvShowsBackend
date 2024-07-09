package com.limelist.tvHistory.services.dataUpdateServices.yandex.api

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.util.*

class SimpleRateLimiting(
    currentRateLimitPerHour: Int
) {
    private val now
        get() = ZonedDateTime.now().toInstant().toEpochMilli()

    private val mutex = Mutex()
    private var lastRequestDate = now

    private val currentRequestsDelayMilliseconds
        get() = 60 * 60 / currentRateLimit * 1000

    var currentRateLimit = currentRateLimitPerHour
        private set

    suspend fun <T> withLock(
        block: suspend () -> T
    ) : T = mutex.withLock {
        val timeSpan = now - lastRequestDate

        if (timeSpan < currentRequestsDelayMilliseconds) {
            delay(currentRequestsDelayMilliseconds - timeSpan);
        }
        lastRequestDate = now
        return block()
    }

    fun updateRateLimit(newRateLimit: Int){
        currentRateLimit = newRateLimit
    }
}