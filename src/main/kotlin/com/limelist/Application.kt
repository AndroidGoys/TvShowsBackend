package com.limelist

import com.limelist.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.util.concurrent.TimeUnit

fun main() {
    val application = embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    );

    Runtime.getRuntime().addShutdownHook(Thread {
        application.stop(1, 5, TimeUnit.SECONDS)
    })

    application.start(wait = true)
}

fun Application.module() {
    val services = configureServices();

    configureHTTP()
    configureRouting(services)
}
