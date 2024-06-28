package com.limelist.plugins

import java.sql.DriverManager;
import io.ktor.server.application.*
import kotlinx.coroutines.sync.Mutex

import com.limelist.ApplicationServices
import com.limelist.tvHistory.TvHistoryServices
import com.limelist.tvHistory.dataAccess.sqlite.repositories.TvChannelsSqliteRepository
import com.limelist.tvHistory.dataAccess.sqlite.repositories.TvShowsSqliteRepository
import com.limelist.tvHistory.services.TvChannelsService;
import com.limelist.tvHistory.services.TvShowsService;


fun Application.configureServices() : ApplicationServices {
    val tvHistoryServices = configureTvHistoryServices()
    return ApplicationServices(tvHistoryServices)
}

fun Application.configureTvHistoryServices(): TvHistoryServices {
    val conn = DriverManager.getConnection("jdbc:sqlite:tvHisotry.sql")
    val mutex = Mutex()

    val channels = TvChannelsSqliteRepository(conn, mutex)
    val shows = TvShowsSqliteRepository(conn, mutex)

    val tvChannelsService = TvChannelsService(channels);
    val tvShowsService = TvShowsService(shows);

    return TvHistoryServices(tvChannelsService, tvShowsService)
}