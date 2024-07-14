package com.limelist

import com.limelist.plugins.*
import com.limelist.slices.auth.services.PBKDF2Hasher
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.FileNotFoundException
import java.util.UUID.randomUUID
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
    val applicationConfig = loadConfig()
    val services = configureServices(
        backgroundServicesContext,
        applicationConfig
    );
    registerBackgroundServices(
        services,
        backgroundServicesContext
    );

    configureHTTP()
    configureRouting(services)
}

@OptIn(ExperimentalSerializationApi::class)
fun loadConfig(): ApplicationConfig {
    val classLoader = Thread.currentThread().contextClassLoader;
    val resourceStream = classLoader.getResourceAsStream("ApplicationConfig.json")
    resourceStream ?: throw FileNotFoundException("ApplicationConfig.json")
    return Json.decodeFromStream<ApplicationConfig>(resourceStream);
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
