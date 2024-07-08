package com.limelist.shared

interface DbLifeCycle {
    suspend fun start()
    suspend fun stop()
}