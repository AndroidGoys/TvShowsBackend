package com.limelist.plugins

import java.sql.DriverManager;
import io.ktor.server.application.*
import kotlinx.coroutines.sync.Mutex

import com.limelist.ApplicationServices
import com.limelist.tvHistory.TvHistoryServices
import com.limelist.tvHistory.dataAccess.sqlite.repositories.TvChannelsSqliteRepository
import com.limelist.tvHistory.dataAccess.sqlite.repositories.TvReleasesSqliteRepository
import com.limelist.tvHistory.dataAccess.sqlite.repositories.TvShowsSqliteRepository
import com.limelist.tvHistory.services.tvChannelServices.TvChannelsService;
import com.limelist.tvHistory.services.tvShowServices.TvShowsService;
import com.limelist.tvHistory.services.dataUpdateServices.JsonSourceDataUpdateService


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
    val releases = TvReleasesSqliteRepository(conn, mutex);

    val tvChannelsService = TvChannelsService(channels);
    val tvShowsService = TvShowsService(shows);

    val dataUpdateService = JsonSourceDataUpdateService(
        channels,
        shows,
        releases
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