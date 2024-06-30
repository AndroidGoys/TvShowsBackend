package com.limelist.tvHistory.services

import com.limelist.shared.IHostedService
import com.limelist.tvHistory.dataAccess.interfaces.TvChannelsRepository
import kotlinx.coroutines.*
import kotlin.time.Duration

class TvDataUpdateHostedService(
    val tvChannelsRepository: TvChannelsRepository,
    val sleepDuration: Duration
) : IHostedService {

    private var job: Job? = null;

    val isRunning get() = job != null

    private suspend fun updateLoop(){
        while (isRunning){
            try {
                delay(sleepDuration)
            }
            finally {
                println("джоба отменена")
            }
        }
    }

    override fun start(){
        if (isRunning)
            return;

        job = GlobalScope.launch {
            updateLoop()
        }

        println("есть джоба")
    }

    override fun stop(){
        if (isRunning) {
            println("отменяем джобу")
            job?.cancel()
            job = null
        }
    }
}