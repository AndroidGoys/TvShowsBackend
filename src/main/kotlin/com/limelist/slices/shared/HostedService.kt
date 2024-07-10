package com.limelist.slices.shared

interface HostedService {
    suspend fun start()
    suspend fun stop()
}
