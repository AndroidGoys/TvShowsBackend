package com.limelist

import com.limelist.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

suspend fun main(){
    val application = embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    );

    application.addShutdownHook {
        application.stop(1, 5, TimeUnit.SECONDS)
    }

    application.start(wait = true)
}

fun Application.module() {
    val backgroundServicesContext: CoroutineContext = Dispatchers.Default
    val services = configureServices(
        backgroundServicesContext
    );
    registerBackgroundServices(
        services,
        backgroundServicesContext
    );

    configureHTTP()
    configureRouting(services)
}

fun Application.registerBackgroundServices(
    services: ApplicationServices,
    coroutineContext: CoroutineContext
) {
    val backgroundServices = services.backgroundServices
    val dataBases = services.databases
    val servicesScope = CoroutineScope(coroutineContext)

    this.environment.monitor.subscribe(ApplicationStarted) {
        servicesScope.launch {
            dataBases.forEach { it.start() }

            val jobs = backgroundServices.map {
                launch { it.start() }
            }
            jobs.joinAll()
            log.info("Background services started")
        }
    }

    this.environment.monitor.subscribe(ApplicationStopped) {
        servicesScope.launch {
            val jobs = backgroundServices.map {
                launch { it.stop() }
            }
            jobs.joinAll()
            dataBases.forEach { it.stop() }
            log.info("Background services stopped")
        }
    }

}
