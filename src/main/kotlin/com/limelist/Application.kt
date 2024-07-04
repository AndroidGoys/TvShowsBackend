package com.limelist

import com.limelist.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

fun main(){
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
    val services = configureServices();
    registerBackgroundServices(services);

    configureHTTP()
    configureRouting(services)
}

@OptIn(DelicateCoroutinesApi::class)
fun Application.registerBackgroundServices(
    services: ApplicationServices
) {
    val backgroundService = services.backgroundServices;

    this.environment.monitor.subscribe(ApplicationStarted) {
        backgroundService.map {
            GlobalScope.launch { it.start() }
        }
        log.info("Background services started")
    }

    this.environment.monitor.subscribe(ApplicationStopped) {
        backgroundService.map {
            GlobalScope.launch { it.start() }
        }
        log.info("Background services stopped")
    }

}
