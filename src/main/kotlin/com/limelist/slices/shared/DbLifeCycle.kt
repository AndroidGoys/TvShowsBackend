package com.limelist.slices.shared

interface DbLifeCycle {
    suspend fun start()
    suspend fun stop()
}