package com.limelist.plugins

import com.limelist.ApplicationConfig
import java.sql.DriverManager;
import io.ktor.server.application.*
import kotlinx.coroutines.sync.Mutex

import com.limelist.ApplicationServices
import com.limelist.slices.tvStore.TvStoreConfig
import com.limelist.slices.tvStore.TvStoreServices
import com.limelist.slices.tvStore.dataAccess.sqlite.repositories.TvChannelsSqliteRepository
import com.limelist.slices.tvStore.dataAccess.sqlite.repositories.TvReleasesSqliteRepository
import com.limelist.slices.tvStore.dataAccess.sqlite.repositories.TvShowsSqliteRepository
import com.limelist.slices.tvStore.dataAccess.sqlite.repositories.TvSqliteDbLifeCycle
import com.limelist.slices.tvStore.services.tvChannelServices.TvChannelsService;
import com.limelist.slices.tvStore.services.tvShowServices.TvShowsService;
import com.limelist.slices.tvStore.services.dataUpdateServices.JsonSourceDataUpdateService
import kotlin.coroutines.CoroutineContext


fun Application.configureServices(
    coroutineContext: CoroutineContext,
    config: ApplicationConfig
) : ApplicationServices {
    val tvHistoryServices = configureTvHistoryServices(
        coroutineContext,
        config.tvStoreConfig
    )
    return ApplicationServices(
        tvHistoryServices,
        tvHistoryServices.backgroundServices,
        tvHistoryServices.databases)
}

fun Application.configureTvHistoryServices(
    coroutineContext: CoroutineContext,
    config: TvStoreConfig
): TvStoreServices {
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
        config.dataUpdateConfig,
        coroutineContext
    );

    return TvStoreServices(
        tvChannelsService,
        tvShowsService,
        listOf(dataUpdateService),
        listOf(dbLifeCycle)
    )
}