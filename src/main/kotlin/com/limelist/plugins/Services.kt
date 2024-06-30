package com.limelist.plugins

import java.sql.DriverManager;
import io.ktor.server.application.*
import kotlinx.coroutines.sync.Mutex

import com.limelist.ApplicationServices
import com.limelist.tvHistory.TvHistoryServices
import com.limelist.tvHistory.dataAccess.sqlite.repositories.TvChannelsSqliteRepository
import com.limelist.tvHistory.dataAccess.sqlite.repositories.TvShowsSqliteRepository
import com.limelist.tvHistory.services.TvChannelsService;
import com.limelist.tvHistory.services.TvDataUpdateHostedService
import com.limelist.tvHistory.services.TvShowsService;
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime


fun Application.configureServices() : ApplicationServices {
    val tvHistoryServices = configureTvHistoryServices()
    return ApplicationServices(
        tvHistoryServices,
        tvHistoryServices.backgroundServices)
}

fun Application.configureTvHistoryServices(): TvHistoryServices {
    val conn = DriverManager.getConnection("jdbc:sqlite:tvHisotry.sql")
    val mutex = Mutex()

    val channels = TvChannelsSqliteRepository(conn, mutex)
    val shows = TvShowsSqliteRepository(conn, mutex)

    val tvChannelsService = TvChannelsService(channels);
    val tvShowsService = TvShowsService(shows);

    val dataUpdateService = TvDataUpdateHostedService(
        channels,
        120.minutes
    );

    this.environment.monitor.subscribe(ApplicationStopped) { application ->
        conn.close()
        //чертово колдунство, как это вообще работает
        application.environment.monitor.unsubscribe(ApplicationStopped) {}
    }
    return TvHistoryServices(
        tvChannelsService,
        tvShowsService,
        listOf(dataUpdateService)
    )
}