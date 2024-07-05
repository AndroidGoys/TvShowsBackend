package com.limelist.shared

interface HostedService {
    suspend fun start()
    suspend fun stop()
}
