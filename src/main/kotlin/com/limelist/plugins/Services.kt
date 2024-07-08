package com.limelist.plugins

import java.sql.DriverManager;
import io.ktor.server.application.*
import kotlinx.coroutines.sync.Mutex

import com.limelist.ApplicationServices
import com.limelist.tvHistory.TvHistoryServices
import com.limelist.tvHistory.dataAccess.sqlite.repositories.TvChannelsSqliteRepository
import com.limelist.tvHistory.dataAccess.sqlite.repositories.TvReleasesSqliteRepository
import com.limelist.tvHistory.dataAccess.sqlite.repositories.TvShowsSqliteRepository
import com.limelist.tvHistory.dataAccess.sqlite.repositories.TvSqliteDbLifeCycle
import com.limelist.tvHistory.services.tvChannelServices.TvChannelsService;
import com.limelist.tvHistory.services.tvShowServices.TvShowsService;
import com.limelist.tvHistory.services.dataUpdateServices.JsonSourceDataUpdateService
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext


fun Application.configureServices(
    coroutineContext: CoroutineContext
) : ApplicationServices {
    val tvHistoryServices = configureTvHistoryServices(coroutineContext)
    return ApplicationServices(
        tvHistoryServices,
        tvHistoryServices.backgroundServices,
        tvHistoryServices.databases)
}

fun Application.configureTvHistoryServices(
    coroutineContext: CoroutineContext
): TvHistoryServices {
    val conn = DriverManager.getConnection("jdbc:sqlite:tvHisotry.sql")
    val mutex = Mutex()

    val dbLifeCycle = TvSqliteDbLifeCycle(conn, mutex)

    val channels = TvChannelsSqliteRepository(conn, mutex)
    val shows = TvShowsSqliteRepository(conn, mutex)
    val releases = TvReleasesSqliteRepository(conn, mutex);

    val tvChannelsService = TvChannelsService(channels);
    val tvShowsService = TvShowsService(shows);

    val dataUpdateService = JsonSourceDataUpdateService(
        channels,
        shows,
        releases,
        coroutineContext
    );

    return TvHistoryServices(
        tvChannelsService,
        tvShowsService,
        listOf(dataUpdateService),
        listOf(dbLifeCycle)
    )
}